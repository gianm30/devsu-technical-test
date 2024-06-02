package com.gian.carrasco.reto.tecnico.handler;

import com.gian.carrasco.reto.tecnico.constant.ErrorsConstants;
import com.gian.carrasco.reto.tecnico.service.CuentaService;
import com.gian.carrasco.reto.tecnico.service.MovimientoService;
import com.gian.carrasco.reto.tecnico.validators.NumbersValidator;
import com.gian.carrasco.reto.tecnico.validators.ReporteValidator;
import com.gian.carrasco.reto.tecnico.validators.ValidationHandler;
import com.gian.carrasco.reto.tecnico.vo.MovimientoRsVO;
import com.gian.carrasco.reto.tecnico.vo.ReporteFechasPorUsuarioRsVO;
import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

@Component
public class ReporteHandler {
    private final MovimientoService movimientoService;
    private final CuentaService cuentaService;
    private final ValidationHandler validationHandler;

    public ReporteHandler(@Autowired MovimientoService movimientoService, @Autowired CuentaService cuentaService, @Autowired ValidationHandler validationHandler) {
        this.movimientoService = movimientoService;
        this.cuentaService = cuentaService;
        this.validationHandler = validationHandler;
    }

    public Mono<ServerResponse> generateReport(@SuppressWarnings("unused") ServerRequest request) {
        Optional<String> optionalDesde = request.queryParam("desde");
        Optional<String> optionalHasta = request.queryParam("hasta");
        Optional<String> optionalIdCliente = request.queryParam("cliente");
        if(optionalDesde.isEmpty() || optionalHasta.isEmpty() || optionalIdCliente.isEmpty())
            return ResponseVO.badRequest(ErrorsConstants.PARAMETROS_FALTANTES);

        if(ReporteValidator.isInvalidDateFormat(optionalDesde.get()))
            return ResponseVO.badRequest(ErrorsConstants.PARAMETROS_INCORRECTOS);

        if(ReporteValidator.isInvalidDateFormat(optionalHasta.get()))
            return ResponseVO.badRequest(ErrorsConstants.PARAMETROS_INCORRECTOS);

        if(NumbersValidator.isNotGreaterThanZero(optionalIdCliente.get()))
            return ResponseVO.badRequest(ErrorsConstants.PARAMETROS_INCORRECTOS);

        LocalDate desde = ReporteValidator.toDate(optionalDesde.get());
        LocalDate hasta = ReporteValidator.toDate(optionalHasta.get());
        Integer idCliente = Integer.parseInt(optionalIdCliente.get());

        return ServerResponse.ok().body(
                cuentaService.findByIdCliente(idCliente)
                        .flatMap(cuenta -> movimientoService.findReport(cuenta.getId(), desde, hasta))
                        .map(this::toVO)
                        .flatMap(this::fillCliente)
                , MovimientoRsVO.class);
    }

    private ReporteFechasPorUsuarioRsVO toVO(MovimientoRsVO vo) {
        return ReporteFechasPorUsuarioRsVO.builder()
                .idCuenta(vo.getCuenta().getId())
                .movimiento(vo.getValor())
                .fecha(vo.getFecha())
                .build();
    }

    private Mono<ReporteFechasPorUsuarioRsVO> fillCliente(ReporteFechasPorUsuarioRsVO vo) {
        Optional<Integer> idCuenta = Optional.ofNullable(vo.getIdCuenta());
        if(idCuenta.isPresent())
            return cuentaService.findById(idCuenta.get())
                    .map(cuenta -> {
                        vo.setCliente(cuenta.getCliente().getNombre());
                        vo.setTipo(cuenta.getTipoCuenta().getDescripcion());
                        vo.setNroCuenta(cuenta.getNumero());
                        vo.setEstado(cuenta.getEstado());
                        vo.setSaldoInicial(cuenta.getSaldoInicial());
                        vo.setSaldoDisponible(cuenta.getSaldoActual());
                        return vo;
                    });
        return Mono.just(vo);
    }
}
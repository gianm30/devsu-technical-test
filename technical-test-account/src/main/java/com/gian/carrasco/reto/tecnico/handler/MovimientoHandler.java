package com.gian.carrasco.reto.tecnico.handler;

import com.gian.carrasco.reto.tecnico.constant.ErrorsConstants;
import com.gian.carrasco.reto.tecnico.enums.TipoMovimientoEnum;
import com.gian.carrasco.reto.tecnico.exception.ControlledException;
import com.gian.carrasco.reto.tecnico.service.CuentaService;
import com.gian.carrasco.reto.tecnico.service.MovimientoService;
import com.gian.carrasco.reto.tecnico.validators.MovimientoValidator;
import com.gian.carrasco.reto.tecnico.validators.ValidationHandler;
import com.gian.carrasco.reto.tecnico.vo.CuentaRsVO;
import com.gian.carrasco.reto.tecnico.vo.MovimientoRqVO;
import com.gian.carrasco.reto.tecnico.vo.MovimientoRsVO;
import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class MovimientoHandler {
    private final MovimientoService service;
    private final CuentaService cuentaService;
    private final ValidationHandler validationHandler;

    public MovimientoHandler(@Autowired MovimientoService service, @Autowired CuentaService cuentaService, @Autowired ValidationHandler validationHandler) {
        this.service = service;
        this.cuentaService = cuentaService;
        this.validationHandler = validationHandler;
    }

    public Mono<ServerResponse> findAll(@SuppressWarnings("unused") ServerRequest request) {
        return ServerResponse.ok().body(service.findAll(), CuentaRsVO.class);
    }


    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(MovimientoRqVO.class)
                .flatMap(MovimientoValidator::validateIfIdExists)
                .flatMap(validationHandler::validateBody)
                .flatMap(service::create)
                .flatMap(ResponseVO::created);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(MovimientoRqVO.class)
                .flatMap(MovimientoValidator::validateIfIdExists)
                .flatMap(validationHandler::validateBody)
                .flatMap(vo -> MovimientoValidator.validateId(vo, id)
                        .flatMap(service::update)
                        .flatMap(ResponseVO::ok)
                );
    }

    public Mono<ServerResponse> patch(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(MovimientoRqVO.class)
                .flatMap(MovimientoValidator::validateIfIdExists)
                .flatMap(rq -> MovimientoValidator.validateId(rq, id))
                .flatMap(rq ->
                        cuentaService.findByNumero(rq.getNroCuenta())
                                .switchIfEmpty(rq.getNroCuenta() == null ? Mono.just(new CuentaRsVO()) : Mono.error(new ControlledException(ErrorsConstants.NRO_CUENTA_NO_EXISTE)))
                                .flatMap(cuenta -> Mono.just(rq)
                                        .map(x -> x.setIdCuenta(cuenta.getId()))
                                        .flatMap(service::patch)
                                        .map(movimiento -> movimiento.setCuenta(cuenta))
                                        .flatMap(ResponseVO::ok)
                                )
                );
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return MovimientoValidator.validateId(id)
                .flatMap(service::delete)
                .then(ResponseVO.noContent());
    }
}
package com.gian.carrasco.reto.tecnico.service.impl;

import com.gian.carrasco.reto.tecnico.constant.ErrorsConstants;
import com.gian.carrasco.reto.tecnico.entity.Cuenta;
import com.gian.carrasco.reto.tecnico.entity.Movimiento;
import com.gian.carrasco.reto.tecnico.entity.TipoMovimiento;
import com.gian.carrasco.reto.tecnico.enums.TipoMovimientoEnum;
import com.gian.carrasco.reto.tecnico.exception.ControlledException;
import com.gian.carrasco.reto.tecnico.exception.IdNotExistsException;
import com.gian.carrasco.reto.tecnico.exception.InvalidIdException;
import com.gian.carrasco.reto.tecnico.repository.CuentaRepository;
import com.gian.carrasco.reto.tecnico.repository.MovimientoRepository;
import com.gian.carrasco.reto.tecnico.repository.TipoMovimientoRepository;
import com.gian.carrasco.reto.tecnico.service.MovimientoService;
import com.gian.carrasco.reto.tecnico.validators.NumbersValidator;
import com.gian.carrasco.reto.tecnico.vo.*;
import com.gian.carrasco.reto.tecnico.webclient.cliente.ClienteWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
public class MovimientoServiceImpl implements MovimientoService {
    private final MovimientoRepository repository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final ClienteWebClient clienteWebClient;

    public MovimientoServiceImpl(@Autowired MovimientoRepository repository, @Autowired TipoMovimientoRepository tipoMovimientoRepository, @Autowired CuentaRepository cuentaRepository, @Autowired ClienteWebClient clienteWebClient) {
        this.repository = repository;
        this.tipoMovimientoRepository = tipoMovimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.clienteWebClient = clienteWebClient;
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovimientoRsVO> findAll() {
        return repository.findAll()
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoMovimiento)
                .flatMap(this::loadCuenta)
                .flatMap(this::loadCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<MovimientoRsVO> findReport(Integer idCuenta, LocalDate desde, LocalDate hasta) {
        return repository.findByIdCuentaAndFechaBetween(idCuenta, desde, hasta)
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoMovimiento)
                .flatMap(this::loadCliente);
    }

    @Override
    public Mono<MovimientoRsVO> create(MovimientoRqVO vo) {
        return cuentaRepository.findByNumero(vo.getNroCuenta())
                .switchIfEmpty(Mono.error(new ControlledException(ErrorsConstants.NRO_CUENTA_NO_EXISTE)))
                .flatMap(cuenta -> Mono.just(vo)
                        .flatMap(movimiento -> calcularSaldoActual(movimiento, cuenta))
                        .flatMap(this::toEntity)
                        .flatMap(movimiento -> saveCuenta(movimiento, cuenta))
                        .flatMap(repository::save)
                        .flatMap(this::toResponse)
                        .flatMap(this::loadTipoMovimiento)
                        .flatMap(this::loadCuenta)
                        .flatMap(this::loadCliente));
    }

    @Override
    public Mono<MovimientoRsVO> update(MovimientoRqVO vo) {
        return repository.findById(vo.getId())
                .switchIfEmpty(Mono.error(IdNotExistsException::new))
                .flatMap(currentMovimiento -> cuentaRepository.findById(currentMovimiento.getIdCuenta())
                        .flatMap(oldCuenta -> devolverMontoMovimiento(currentMovimiento, oldCuenta))
                        .flatMap(this::saveCuenta)
                        .flatMap(oldCuentaPersisted -> cuentaRepository.findByNumero(vo.getNroCuenta()))
                        .switchIfEmpty(Mono.error(new ControlledException(ErrorsConstants.NRO_CUENTA_NO_EXISTE)))
                        .flatMap(newCuenta -> calcularSaldoActual(vo, newCuenta).map(x -> newCuenta))
                        .flatMap(this::saveCuenta)
                        .map(newCuenta -> {
                            currentMovimiento.setIdCuenta(newCuenta.getId());
                            currentMovimiento.setIdTipoMovimiento(vo.getIdTipoMovimiento());
                            currentMovimiento.setValor(vo.getValor());
                            currentMovimiento.setFecha(vo.getFecha());
                            return currentMovimiento;
                        })
                )
                .flatMap(repository::save)
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoMovimiento);
    }

    @Override
    public Mono<MovimientoRsVO> patch(MovimientoRqVO vo) {
        return repository.findById(vo.getId())
                .switchIfEmpty(Mono.error(IdNotExistsException::new))
                .flatMap(currentMovimiento -> cuentaRepository.findById(currentMovimiento.getIdCuenta())
                        .flatMap(oldCuenta -> devolverMontoMovimiento(currentMovimiento, oldCuenta))
                        .flatMap(this::saveCuenta)
                        .map(oldCuenta -> Optional.ofNullable(vo.getNroCuenta()).orElse(oldCuenta.getNumero()))
                        .flatMap(newNumeroCuenta -> cuentaRepository.findByNumero(newNumeroCuenta))
                        .switchIfEmpty(Mono.error(new ControlledException(ErrorsConstants.NRO_CUENTA_NO_EXISTE)))
                        .flatMap(newCuenta -> calcularSaldoActual(vo, newCuenta).map(x -> newCuenta))
                        .flatMap(this::saveCuenta)
                        .map(newCuenta -> {
                            currentMovimiento.setIdCuenta(newCuenta.getId());
                            Optional.ofNullable(vo.getIdTipoMovimiento()).ifPresent(currentMovimiento::setIdTipoMovimiento);
                            Optional.ofNullable(vo.getValor()).ifPresent(currentMovimiento::setValor);
                            Optional.ofNullable(vo.getFecha()).ifPresent(currentMovimiento::setFecha);
                            return currentMovimiento;
                        })
                )
                .flatMap(repository::save)
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoMovimiento);
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(IdNotExistsException::new))
                .flatMap(currentMovimiento -> cuentaRepository.findById(currentMovimiento.getIdCuenta())
                        .flatMap(oldCuenta -> devolverMontoMovimiento(currentMovimiento, oldCuenta))
                        .flatMap(this::saveCuenta)
                        .map(newCuenta -> currentMovimiento))
                .flatMap(repository::delete);
    }

    private Mono<TipoMovimientoRsVO> toTipoMovimientoVO(TipoMovimiento bd) {
        return Mono.just(TipoMovimientoRsVO.builder()
                .id(bd.getId())
                .descripcion(bd.getDescripcion())
                .build());
    }

    private Mono<Cuenta> devolverMontoMovimiento(Movimiento movimiento, Cuenta cuenta) {
        Double nuevoSaldoActual;
        if(movimiento.getIdTipoMovimiento() == TipoMovimientoEnum.RETIRO.getId()) {
            nuevoSaldoActual = cuenta.getSaldoActual() + movimiento.getValor();
        } else if(movimiento.getIdTipoMovimiento() == TipoMovimientoEnum.DEPOSITO.getId())
            nuevoSaldoActual = cuenta.getSaldoActual() - movimiento.getValor();
        else
            return Mono.error(new ControlledException(ErrorsConstants.ID_TIPO_MOVIMIENTO_NO_ELABORADO));

        if(nuevoSaldoActual < 0)
            return Mono.error(new ControlledException(ErrorsConstants.SALDO_INSUFICIENTE));
        else {
            cuenta.setSaldoActual(nuevoSaldoActual);
            return Mono.just(cuenta);
        }
    }

    private Mono<MovimientoRqVO> calcularSaldoActual(MovimientoRqVO rq, Cuenta cuenta) {
        Double nuevoSaldoActual;
        if(rq.getIdTipoMovimiento() == TipoMovimientoEnum.RETIRO.getId()) {
            nuevoSaldoActual = cuenta.getSaldoActual() - rq.getValor();
        } else if(rq.getIdTipoMovimiento() == TipoMovimientoEnum.DEPOSITO.getId())
            nuevoSaldoActual = cuenta.getSaldoActual() + rq.getValor();
        else
            return Mono.error(new ControlledException(ErrorsConstants.ID_TIPO_MOVIMIENTO_NO_ELABORADO));

        if(nuevoSaldoActual < 0)
            return Mono.error(new ControlledException(ErrorsConstants.SALDO_INSUFICIENTE));
        else {
            rq.setIdCuenta(cuenta.getId());
            cuenta.setSaldoActual(nuevoSaldoActual);
            return Mono.just(rq);
        }
    }

    private Mono<Cuenta> saveCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    private Mono<Movimiento> saveCuenta(Movimiento movimiento, Cuenta cuenta) {
        return cuentaRepository.save(cuenta).map(x -> movimiento);
    }

    private Mono<TipoMovimientoRsVO> findTipoMovimiento(Integer id) {
        Optional<Integer> optional = Optional.ofNullable(id).filter(NumbersValidator::isGreaterThanZero);
        if(optional.isEmpty())
            return Mono.error(new InvalidIdException(ErrorsConstants.ID_TIPO_MOVIMIENTO_NO_ES_VALIDO));

        return tipoMovimientoRepository.findById(id)
                .switchIfEmpty(Mono.error(new IdNotExistsException(ErrorsConstants.ID_TIPO_MOVIMIENTO_NO_EXISTE)))
                .flatMap(this::toTipoMovimientoVO)
                .onErrorResume(Mono::error);
    }

    private Mono<MovimientoRsVO> loadTipoMovimiento(MovimientoRsVO vo) {
        Optional<Integer> optional = Optional.ofNullable(vo.getTipoMovimiento()).map(TipoMovimientoRsVO::getId);
        if(optional.isEmpty())
            return Mono.just(vo);

        return tipoMovimientoRepository.findById(optional.get()).map(x -> {
            vo.getTipoMovimiento().setDescripcion(x.getDescripcion());
            return vo;
        }).onErrorResume(throwable -> Mono.just(vo));
    }

    private Mono<MovimientoRsVO> loadCuenta(MovimientoRsVO vo) {
        Optional<Integer> optional = Optional.ofNullable(vo.getCuenta()).map(CuentaRsVO::getId);
        if(optional.isEmpty())
            return Mono.just(vo);

        return cuentaRepository.findById(optional.get()).map(x -> {
            vo.getCuenta().setNumero(x.getNumero());
            vo.getCuenta().setSaldoInicial(x.getSaldoInicial());
            vo.getCuenta().setSaldoActual(x.getSaldoActual());
            vo.getCuenta().setEstado(x.getEstado());
            vo.getCuenta().setCliente(ClienteRsVO.builder().id(x.getIdCliente()).build());
            return vo;
        }).onErrorResume(throwable -> Mono.just(vo));
    }

    private Mono<MovimientoRsVO> loadCliente(MovimientoRsVO vo) {
        Optional<Integer> optionalId = Optional.ofNullable(vo.getCuenta()).map(CuentaRsVO::getCliente).map(ClienteRsVO::getId);
        if(optionalId.isEmpty())
            return Mono.just(vo);

        return clienteWebClient.findById(optionalId.get()).map(y -> {
            vo.getCuenta().getCliente().setNombre(y.getNombre());
            return vo;
        }).onErrorResume(throwable -> Mono.just(vo));
    }

    private Mono<MovimientoRsVO> toResponse(Movimiento obj) {
        TipoMovimientoRsVO tipoMovimiento = Optional.ofNullable(obj.getIdTipoMovimiento())
                .filter(NumbersValidator::isGreaterThanZero)
                .map(id -> TipoMovimientoRsVO.builder().id(id).build())
                .orElse(null);

        return Mono.just(MovimientoRsVO.builder()
                .id(obj.getId())
                .cuenta(CuentaRsVO.builder().id(obj.getIdCuenta()).build())
                .tipoMovimiento(tipoMovimiento)
                .valor(obj.getValor())
                .fecha(obj.getFecha())
                .build());
    }

    private Mono<Movimiento> toEntity(MovimientoRqVO obj) {
        return Mono.just(Movimiento.builder()
                .id(obj.getId())
                .idCuenta(obj.getIdCuenta())
                .idTipoMovimiento(obj.getIdTipoMovimiento())
                .valor(obj.getValor())
                .fecha(obj.getFecha())
                .build());
    }
}
package com.gian.carrasco.reto.tecnico.service.impl;

import com.gian.carrasco.reto.tecnico.entity.Cuenta;
import com.gian.carrasco.reto.tecnico.entity.TipoCuenta;
import com.gian.carrasco.reto.tecnico.exception.IdNotExistsException;
import com.gian.carrasco.reto.tecnico.exception.InvalidIdException;
import com.gian.carrasco.reto.tecnico.constant.ErrorsConstants;
import com.gian.carrasco.reto.tecnico.exception.ControlledException;
import com.gian.carrasco.reto.tecnico.repository.CuentaRepository;
import com.gian.carrasco.reto.tecnico.repository.TipoCuentaRepository;
import com.gian.carrasco.reto.tecnico.service.CuentaService;
import com.gian.carrasco.reto.tecnico.validators.NumbersValidator;
import com.gian.carrasco.reto.tecnico.vo.ClienteRsVO;
import com.gian.carrasco.reto.tecnico.vo.CuentaRqVO;
import com.gian.carrasco.reto.tecnico.vo.CuentaRsVO;
import com.gian.carrasco.reto.tecnico.vo.TipoCuentaRsVO;
import com.gian.carrasco.reto.tecnico.webclient.cliente.ClienteWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CuentaServiceImpl implements CuentaService {
    private static final Logger log = LoggerFactory.getLogger(CuentaServiceImpl.class);
    private final CuentaRepository repository;
    private final TipoCuentaRepository tipoCuentaRepository;
    private final ClienteWebClient clienteWebClient;

    public CuentaServiceImpl(@Autowired CuentaRepository repository, @Autowired TipoCuentaRepository tipoCuentaRepository, @Autowired ClienteWebClient clienteWebClient) {
        this.repository = repository;
        this.tipoCuentaRepository = tipoCuentaRepository;
        this.clienteWebClient = clienteWebClient;
    }

    @Override
    public Flux<CuentaRsVO> findAll() {
        return repository.findAll()
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoCuenta)
                .flatMap(this::loadCliente);
    }

    @Override
    public Mono<CuentaRsVO> findById(Integer id) {
        return repository.findById(id)
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoCuenta)
                .flatMap(this::loadCliente);
    }

    @Override
    public Flux<CuentaRsVO> findByIdCliente(Integer idCliente) {
        return repository.findByIdCliente(idCliente)
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoCuenta)
                .flatMap(this::loadCliente);
    }

    @Override
    public Mono<CuentaRsVO> findByNumero(String numero) {
        return repository.findByNumero(numero)
                .switchIfEmpty(Mono.error(new ControlledException(ErrorsConstants.NRO_CUENTA_NO_EXISTE)))
                .flatMap(this::toResponse)
                .flatMap(this::loadTipoCuenta)
                .flatMap(this::loadCliente);
    }

    @Override
    public Mono<CuentaRsVO> create(CuentaRqVO vo) {
        return findForeignKeys(vo).flatMap(values -> toEntity(vo)
                .flatMap(repository::save)
                .flatMap(this::toResponse)
                .map(obj -> obj.setTipoCuenta(values.getTipoCuenta()))
                .map(obj -> obj.setCliente(values.getCliente()))
                .flatMap(this::loadCliente)
        );
    }

    @Override
    public Mono<CuentaRsVO> update(CuentaRqVO vo) {
        return repository.findById(vo.getId())
                .map(bd -> {
                    bd.setNumero(vo.getNumero());
                    bd.setIdTipoCuenta(vo.getIdTipoCuenta());
                    bd.setSaldoInicial(vo.getSaldoInicial());
                    bd.setSaldoActual(vo.getSaldoInicial());
                    bd.setEstado(vo.getEstado());
                    bd.setIdCliente(vo.getIdCliente());
                    return bd;
                })
                .flatMap(bd -> findForeignKeys(vo)
                        .flatMap(values -> repository.save(bd))
                        .flatMap(this::toResponse)
                        .flatMap(this::loadTipoCuenta)
                        .flatMap(this::loadCliente)
                        .switchIfEmpty(Mono.error(IdNotExistsException::new))
                );
    }

    @Override
    public Mono<CuentaRsVO> patch(CuentaRqVO vo) {
        return repository.findById(vo.getId())
                .map(bd -> {
                    Optional.ofNullable(vo.getNumero()).ifPresent(bd::setNumero);
                    Optional.ofNullable(vo.getIdTipoCuenta()).ifPresent(bd::setIdTipoCuenta);
                    Optional.ofNullable(vo.getSaldoInicial()).ifPresent(bd::setSaldoInicial);
                    Optional.ofNullable(vo.getSaldoInicial()).ifPresent(bd::setSaldoActual);
                    Optional.ofNullable(vo.getEstado()).ifPresent(bd::setEstado);
                    Optional.ofNullable(vo.getIdCliente()).ifPresent(bd::setIdCliente);
                    return bd;
                })
                .flatMap(bd -> findForeignKeys(vo)
                        .flatMap(values -> repository.save(bd))
                        .flatMap(this::toResponse)
                        .flatMap(this::loadTipoCuenta)
                        .flatMap(this::loadCliente)
                        .switchIfEmpty(Mono.error(IdNotExistsException::new))
                );
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(IdNotExistsException::new))
                .flatMap(repository::delete);
    }

    private Mono<TipoCuentaRsVO> toTipoCuentaVO(TipoCuenta bd) {
        return Mono.just(TipoCuentaRsVO.builder()
                .id(bd.getId())
                .descripcion(bd.getDescripcion())
                .build());
    }

    private Mono<ClienteRsVO> toClienteVO(com.gian.carrasco.reto.tecnico.webclient.vo.ClienteRsVO bd) {
        return Mono.just(ClienteRsVO.builder()
                .id(bd.getId())
                .nombre(bd.getNombre())
                .build());
    }

    private Mono<CuentaRsVO> findForeignKeys(CuentaRqVO vo) {
        Mono<TipoCuentaRsVO> tipoCuentaMono = findTipoCuenta(vo.getIdTipoCuenta());
        Mono<ClienteRsVO> clienteMono = findCliente(vo.getIdCliente());

        Mono<CuentaRsVO> transaction = tipoCuentaMono.zipWith(clienteMono, (a, b) -> CuentaRsVO.builder()
                .tipoCuenta(a)
                .cliente(b)
                .build());

        Mono<Cuenta> nroCuentaMono = repository.findByNumero(vo.getNumero());
        return nroCuentaMono.flatMap(existingAccount -> {
                    if(isCreate(vo) || accountSentNotBelongsToId(vo, existingAccount))
                        return Mono.error(new ControlledException(ErrorsConstants.NRO_CUENTA_YA_EXISTE));
                    return transaction;
                })
                .switchIfEmpty(transaction);
    }

    private boolean isCreate(CuentaRqVO vo) {
        return vo.getId() == null;
    }

    private boolean accountSentNotBelongsToId(CuentaRqVO vo, Cuenta existingAccount) {
        return vo.getId() != existingAccount.getId();
    }

    private Mono<TipoCuentaRsVO> findTipoCuenta(Integer id) {
        Optional<Integer> optional = Optional.ofNullable(id).filter(NumbersValidator::isGreaterThanZero);
        if(optional.isEmpty())
            return Mono.error(new InvalidIdException(ErrorsConstants.ID_TIPO_CUENTA_NO_ES_VALIDO));

        return tipoCuentaRepository.findById(id)
                .switchIfEmpty(Mono.error(new IdNotExistsException(ErrorsConstants.ID_TIPO_CUENTA_NO_EXISTE)))
                .flatMap(this::toTipoCuentaVO)
                .onErrorResume(Mono::error);
    }

    private Mono<ClienteRsVO> findCliente(Integer id) {
        Optional<Integer> optionalId = Optional.ofNullable(id).filter(NumbersValidator::isGreaterThanZero);
        if(optionalId.isEmpty())
            return Mono.error(new InvalidIdException(ErrorsConstants.ID_CLIENTE_NO_ES_VALIDO));

        return clienteWebClient.findById(optionalId.get())
                .flatMap(this::toClienteVO)
                .onErrorResume(WebClientResponseException.class, e -> {
                    if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                        return Mono.error(new IdNotExistsException(ErrorsConstants.ID_CLIENTE_NO_EXISTE));
                    return Mono.error(e);
                });
    }

    private Mono<CuentaRsVO> loadTipoCuenta(CuentaRsVO vo) {
        Optional<Integer> optional = Optional.ofNullable(vo.getTipoCuenta()).map(TipoCuentaRsVO::getId);
        if(optional.isEmpty())
            return Mono.just(vo);

        return tipoCuentaRepository.findById(optional.get()).map(x -> {
            vo.getTipoCuenta().setDescripcion(x.getDescripcion());
            return vo;
        }).onErrorResume(throwable -> Mono.just(vo));
    }

    private Mono<CuentaRsVO> loadCliente(CuentaRsVO vo) {
        Optional<Integer> optionalId = Optional.ofNullable(vo.getCliente()).map(ClienteRsVO::getId);
        if(optionalId.isEmpty())
            return Mono.just(vo);

        return clienteWebClient.findById(optionalId.get()).map(y -> {
            vo.getCliente().setNombre(y.getNombre());
            return vo;
        }).onErrorResume(throwable -> Mono.just(vo));
    }

    private Mono<CuentaRsVO> toResponse(Cuenta entity) {
        TipoCuentaRsVO tipoCuenta = Optional.ofNullable(entity.getIdTipoCuenta())
                .filter(NumbersValidator::isGreaterThanZero)
                .map(id -> TipoCuentaRsVO.builder().id(id).build())
                .orElse(null);

        ClienteRsVO cliente = Optional.ofNullable(entity.getIdCliente())
                .filter(NumbersValidator::isGreaterThanZero)
                .map(id -> ClienteRsVO.builder().id(id).build())
                .orElse(null);

        return Mono.just(CuentaRsVO.builder()
                .id(entity.getId())
                .numero(entity.getNumero())
                .tipoCuenta(tipoCuenta)
                .saldoInicial(entity.getSaldoInicial())
                .saldoActual(entity.getSaldoActual())
                .estado(entity.getEstado())
                .cliente(cliente)
                .build());
    }

    private Mono<Cuenta> toEntity(CuentaRqVO obj) {
        return Mono.just(Cuenta.builder()
                .id(obj.getId())
                .numero(obj.getNumero())
                .idTipoCuenta(obj.getIdTipoCuenta())
                .saldoInicial(obj.getSaldoInicial())
                .saldoActual(obj.getSaldoInicial())
                .estado(obj.getEstado())
                .idCliente(obj.getIdCliente())
                .build());
    }
}
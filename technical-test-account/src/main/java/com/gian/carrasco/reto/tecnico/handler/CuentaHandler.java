package com.gian.carrasco.reto.tecnico.handler;

import com.gian.carrasco.reto.tecnico.constant.ErrorsConstants;
import com.gian.carrasco.reto.tecnico.service.CuentaService;
import com.gian.carrasco.reto.tecnico.validators.ClienteValidator;
import com.gian.carrasco.reto.tecnico.validators.ValidationHandler;
import com.gian.carrasco.reto.tecnico.vo.CuentaRqVO;
import com.gian.carrasco.reto.tecnico.vo.CuentaRsVO;
import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class CuentaHandler {
    private final ValidationHandler validationHandler;
    private final CuentaService service;

    public CuentaHandler(@Autowired CuentaService service, @Autowired ValidationHandler validationHandler) {
        this.service = service;
        this.validationHandler = validationHandler;
    }

    public Mono<ServerResponse> find(@SuppressWarnings("unused") ServerRequest request) {
        Optional<String> numeroCuenta = request.queryParam("numeroCuenta");
        if(numeroCuenta.isEmpty())
            return ServerResponse.ok().body(service.findAll(), CuentaRsVO.class);
        else
            return ServerResponse.ok().body(service.findByNumero(numeroCuenta.get()), CuentaRsVO.class);
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(CuentaRqVO.class)
                .flatMap(ClienteValidator::validateIfIdExists)
                .flatMap(validationHandler::validateBody)
                .flatMap(service::create)
                .flatMap(ResponseVO::created)
                .switchIfEmpty(ResponseVO.badRequest("empty"));
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(CuentaRqVO.class)
                .flatMap(validationHandler::validateBody)
                .flatMap(vo -> ClienteValidator.validateId(vo, id))
                .flatMap(service::update)
                .flatMap(ResponseVO::ok);
    }

    public Mono<ServerResponse> patch(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(CuentaRqVO.class)
                .flatMap(vo -> ClienteValidator.validateId(vo, id))
                .flatMap(service::patch)
                .flatMap(ResponseVO::ok);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ClienteValidator.validateId(id)
                .flatMap(service::delete)
                .then(ResponseVO.noContent());
    }
}
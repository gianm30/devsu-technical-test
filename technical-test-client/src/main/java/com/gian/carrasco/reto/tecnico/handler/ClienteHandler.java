package com.gian.carrasco.reto.tecnico.handler;

import com.gian.carrasco.reto.tecnico.service.ClienteService;
import com.gian.carrasco.reto.tecnico.validators.ClienteValidator;
import com.gian.carrasco.reto.tecnico.validators.ValidationHandler;
import com.gian.carrasco.reto.tecnico.vo.ClienteRqVO;
import com.gian.carrasco.reto.tecnico.vo.ClienteRsVO;
import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ClienteHandler {
    private final ValidationHandler validationHandler;
    private final ClienteService clienteService;

    public ClienteHandler(@Autowired ClienteService clienteService, @Autowired ValidationHandler validationHandler) {
        this.clienteService = clienteService;
        this.validationHandler = validationHandler;
    }

    public Mono<ServerResponse> findAll(@SuppressWarnings("unused") ServerRequest request) {
        return ServerResponse.ok().body(clienteService.findAll(), ClienteRsVO.class);
    }

    public Mono<ServerResponse> findById(ServerRequest request) {
        String idPath = request.pathVariable("id");
        return ClienteValidator.validateId(idPath)
                .flatMap(id -> ServerResponse.ok().body(clienteService.findById(id), ClienteRsVO.class));
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(ClienteRqVO.class)
                .flatMap(ClienteValidator::validateIfIdExists)
                .flatMap(validationHandler::validateBody)
                .flatMap(clienteService::create)
                .flatMap(ResponseVO::created);
    }

    public Mono<ServerResponse> update(ServerRequest request) {
        String id = request.pathVariable("id");

        return request.bodyToMono(ClienteRqVO.class)
                .flatMap(validationHandler::validateBody)
                .flatMap(vo -> ClienteValidator.validateId(vo, id))
                .flatMap(clienteService::update)
                .flatMap(ResponseVO::ok);
    }

    public Mono<ServerResponse> patch(ServerRequest request) {
        String id = request.pathVariable("id");
        return request.bodyToMono(ClienteRqVO.class)
                .flatMap(vo -> ClienteValidator.validateId(vo, id))
                .flatMap(clienteService::patch)
                .flatMap(ResponseVO::ok);
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        String id = request.pathVariable("id");
        return ClienteValidator.validateId(id)
                .flatMap(clienteService::delete)
                .then(ResponseVO.noContent());
    }
}
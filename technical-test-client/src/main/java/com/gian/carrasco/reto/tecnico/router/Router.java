package com.gian.carrasco.reto.tecnico.router;

import com.gian.carrasco.reto.tecnico.handler.ClienteHandler;
import com.gian.carrasco.reto.tecnico.constant.Rutas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class Router {
    private final ClienteHandler clienteHandler;

    public Router(@Autowired ClienteHandler clienteHandler) {
        this.clienteHandler = clienteHandler;
    }

    @Bean
    public RouterFunction<ServerResponse> routeAlumno() {
        return route(GET(Rutas.CLIENTES), clienteHandler::findAll)
                .andRoute(GET(Rutas.CLIENTES + Rutas.PATH.ID), clienteHandler::findById)
                .andRoute(POST(Rutas.CLIENTES), clienteHandler::create)
                .andRoute(PUT(Rutas.CLIENTES + Rutas.PATH.ID), clienteHandler::update)
                .andRoute(PATCH(Rutas.CLIENTES + Rutas.PATH.ID), clienteHandler::patch)
                .andRoute(DELETE(Rutas.CLIENTES + Rutas.PATH.ID), clienteHandler::delete);
    }
}

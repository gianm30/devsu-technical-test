package com.gian.carrasco.reto.tecnico.router;

import com.gian.carrasco.reto.tecnico.handler.MovimientoHandler;
import com.gian.carrasco.reto.tecnico.constant.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterMovimientos {
    private final MovimientoHandler handler;

    public RouterMovimientos(@Autowired MovimientoHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> movimientos() {
        return route(GET(Paths.MOVIMIENTOS), handler::findAll)
                .andRoute(POST(Paths.MOVIMIENTOS), handler::create)
                .andRoute(PUT(Paths.MOVIMIENTOS + Paths.PATH.ID), handler::update)
                .andRoute(PATCH(Paths.MOVIMIENTOS + Paths.PATH.ID), handler::patch)
                .andRoute(DELETE(Paths.MOVIMIENTOS + Paths.PATH.ID), handler::delete);
    }
}

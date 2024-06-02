package com.gian.carrasco.reto.tecnico.router;

import com.gian.carrasco.reto.tecnico.handler.CuentaHandler;
import com.gian.carrasco.reto.tecnico.constant.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterCuentas {
    private final CuentaHandler handler;

    public RouterCuentas(@Autowired CuentaHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> cuentas() {
        return route(GET(Paths.CUENTAS), handler::find)
                .andRoute(POST(Paths.CUENTAS), handler::create)
                .andRoute(PUT(Paths.CUENTAS + Paths.PATH.ID), handler::update)
                .andRoute(PATCH(Paths.CUENTAS + Paths.PATH.ID), handler::patch)
                .andRoute(DELETE(Paths.CUENTAS + Paths.PATH.ID), handler::delete);
    }
}

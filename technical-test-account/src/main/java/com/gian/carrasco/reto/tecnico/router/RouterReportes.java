package com.gian.carrasco.reto.tecnico.router;

import com.gian.carrasco.reto.tecnico.handler.ReporteHandler;
import com.gian.carrasco.reto.tecnico.constant.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterReportes {
    private final ReporteHandler handler;

    public RouterReportes(@Autowired ReporteHandler handler) {
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> reportes() {
        return route(GET(Paths.REPORTES), handler::generateReport);
    }
}

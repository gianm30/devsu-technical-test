package com.gian.carrasco.reto.tecnico.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO {
    private boolean error;
    private String mensaje;
    private Object datos;

    public static Mono<ServerResponse> ok(Object datos) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResponseVO.builder()
                        .error(false)
                        .datos(datos)
                        .build());
    }

    public static Mono<ServerResponse> created(Object datos) {
        return ServerResponse.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(ResponseVO.builder()
                        .error(false)
                        .datos(datos)
                        .build());
    }

    public static Mono<ServerResponse> noContent() {
        return ServerResponse.noContent().build();
    }

    public static Mono<ServerResponse> badRequest(String message) {
        return ServerResponse.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(error(message));
    }

    public static Mono<ServerResponse> notFound(String message) {
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(error(message));
    }

    public static Mono<ServerResponse> internalServerError(String message) {
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(error(message));
    }

    private static ResponseVO error(String mensaje) {
        return ResponseVO.builder()
                .error(true)
                .mensaje(mensaje)
                .build();
    }
}

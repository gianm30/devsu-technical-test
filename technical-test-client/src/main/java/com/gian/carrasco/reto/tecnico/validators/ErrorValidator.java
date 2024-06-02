package com.gian.carrasco.reto.tecnico.validators;

import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorValidator {
    public static Mono<ServerResponse> capturar(WebExchangeBindException e) {
        Map<String, String> errors = new HashMap<>();

        e.getFieldErrors().forEach(fieldError -> errors.put(fieldError.getField(), fieldError.getDefaultMessage()));
        String error = errors.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", "));

        return ResponseVO.badRequest(error);
    }
}

package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class MissingFieldException extends RuntimeException {
    private final Errors errors;

    public MissingFieldException(Errors errors) {
        this.errors = errors;
    }
}

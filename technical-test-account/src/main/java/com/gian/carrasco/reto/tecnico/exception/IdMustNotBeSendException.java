package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;

@Getter
public class IdMustNotBeSendException extends ControlledException {
    private static final String DEFAULT_MESSAGE = "Id no es un campo válido";

    public IdMustNotBeSendException() {
        super(DEFAULT_MESSAGE);
    }
}

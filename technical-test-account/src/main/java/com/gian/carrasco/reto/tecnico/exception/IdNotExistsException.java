package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;

@Getter
public class IdNotExistsException extends ControlledException {
    private static final String DEFAULT_MESSAGE = "Id no existe";

    public IdNotExistsException() {
        super(DEFAULT_MESSAGE);
    }

    public IdNotExistsException(String message) {
        super(message);
    }
}

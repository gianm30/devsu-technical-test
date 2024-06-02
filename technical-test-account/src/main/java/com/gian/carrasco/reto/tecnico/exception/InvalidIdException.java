package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;

@Getter
public class InvalidIdException extends ControlledException {
    private static final String DEFAULT_MESSAGE = "Id no es válido";

    public InvalidIdException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidIdException(String message) {
        super(message);
    }
}

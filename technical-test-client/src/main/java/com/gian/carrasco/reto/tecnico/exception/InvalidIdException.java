package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;

@Getter
public class InvalidIdException extends ControlledException {
    private static final String MENSAJE = "Id no es válido";

    public InvalidIdException() {
        super(MENSAJE);
    }
}

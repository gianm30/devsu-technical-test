package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;

@Getter
public class IdNotExistsException extends ControlledException {
    private static final String MENSAJE = "Id no existe";

    public IdNotExistsException() {
        super(MENSAJE);
    }
}

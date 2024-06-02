package com.gian.carrasco.reto.tecnico.exception;

import lombok.Getter;

@Getter
public class ControlledException extends RuntimeException {
    public ControlledException(String mensaje) {
        super(mensaje);
    }
}

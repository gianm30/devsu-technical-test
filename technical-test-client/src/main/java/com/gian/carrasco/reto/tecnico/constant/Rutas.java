package com.gian.carrasco.reto.tecnico.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Rutas {
    public static final String CLIENTES = "/clientes";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PATH {
        public static final String ID = "/{id}";
    }
}

package com.gian.carrasco.reto.tecnico.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Paths {
    public static final String CUENTAS = "/cuentas";
    public static final String MOVIMIENTOS = "/movimientos";
    public static final String REPORTES = "/reportes";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class PATH {
        public static final String ID = "/{id}";
    }
}

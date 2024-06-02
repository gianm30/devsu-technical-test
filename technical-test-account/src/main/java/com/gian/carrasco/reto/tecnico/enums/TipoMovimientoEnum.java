package com.gian.carrasco.reto.tecnico.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TipoMovimientoEnum {
    RETIRO(1), DEPOSITO(2);

    private final int id;
}

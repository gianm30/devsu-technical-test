package com.gian.carrasco.reto.tecnico.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorsConstants {
    public static final String ERROR_GENERICO = "Tuvimos problemas con el servicio";
    public static final String SALDO_INSUFICIENTE = "Saldo no disponible.";
    public static final String NRO_CUENTA_YA_EXISTE = "Número de cuenta ya existe.";
    public static final String NRO_CUENTA_NO_EXISTE = "Número de cuenta no existe.";
    public static final String ID_TIPO_CUENTA_NO_EXISTE = "El id del tipo de cuenta no existe.";
    public static final String ID_TIPO_CUENTA_NO_ES_VALIDO = "El id del tipo de cuenta no es válido.";
    public static final String ID_TIPO_MOVIMIENTO_NO_EXISTE = "El id del tipo de movimiento no existe.";
    public static final String ID_TIPO_MOVIMIENTO_NO_ES_VALIDO = "El id del tipo de movimiento no es válido.";
    public static final String ID_TIPO_MOVIMIENTO_NO_ELABORADO = "El id del tipo de movimiento no está elaborado.";
    public static final String ID_CLIENTE_NO_EXISTE = "El id del cliente no existe.";
    public static final String ID_CLIENTE_NO_ES_VALIDO = "El id del cliente no es válido.";
    public static final String PARAMETROS_FALTANTES = "No ha enviado los parámetros requeridos";
    public static final String PARAMETROS_INCORRECTOS = "No ha enviado los valores correctos";
}

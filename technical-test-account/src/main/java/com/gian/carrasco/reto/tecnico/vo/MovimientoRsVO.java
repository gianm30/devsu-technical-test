package com.gian.carrasco.reto.tecnico.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoRsVO {
    private Integer id;
    private CuentaRsVO cuenta;
    private TipoMovimientoRsVO tipoMovimiento;
    private Double valor;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double saldo;
    private LocalDate fecha;

    public MovimientoRsVO setCuenta(CuentaRsVO cuenta) {
        this.cuenta = cuenta;
        return this;
    }
}

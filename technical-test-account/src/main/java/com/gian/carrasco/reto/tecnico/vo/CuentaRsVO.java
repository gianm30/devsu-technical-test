package com.gian.carrasco.reto.tecnico.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class CuentaRsVO {
    private Integer id;
    private String numero;
    private TipoCuentaRsVO tipoCuenta;
    private Double saldoInicial;
    private Double saldoActual;
    private Boolean estado;
    private ClienteRsVO cliente;

    public CuentaRsVO setTipoCuenta(TipoCuentaRsVO tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public CuentaRsVO setCliente(ClienteRsVO cliente) {
        this.cliente = cliente;
        return this;
    }
}

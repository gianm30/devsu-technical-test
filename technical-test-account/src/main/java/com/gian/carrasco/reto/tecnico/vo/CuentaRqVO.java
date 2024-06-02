package com.gian.carrasco.reto.tecnico.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuentaRqVO {
    private Integer id;
    @NotBlank
    @Size(min = 6, max = 20)
    private String numero;
    @NotNull
    @Min(0)
    private Integer idTipoCuenta;
    @NotNull
    @Min(0)
    private Double saldoInicial;
    @NotNull
    private Boolean estado;
    @NotNull
    private Integer idCliente;
}

package com.gian.carrasco.reto.tecnico.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Cuenta {
    @Id
    private Integer id;
    private String numero;
    private Integer idTipoCuenta;
    private Double saldoInicial;
    private Double saldoActual;
    private Boolean estado;
    private Integer idCliente;
}

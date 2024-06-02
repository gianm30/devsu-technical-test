package com.gian.carrasco.reto.tecnico.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonPropertyOrder({
        "Fecha", "Cliente", "Numero Cuenta", "Tipo",//
        "Saldo Inicial", "Estado", "Movimiento", "Saldo Disponible"
})
public class ReporteFechasPorUsuarioRsVO {
    @JsonIgnore
    private Integer idCuenta;
    @JsonProperty(value = "Fecha")
    private LocalDate fecha;
    @JsonProperty(value = "Cliente")
    private String cliente;
    @JsonProperty(value = "Numero Cuenta")
    private String nroCuenta;
    @JsonProperty(value = "Tipo")
    private String tipo;
    @JsonProperty(value = "Saldo Inicial")
    private Double saldoInicial;
    @JsonProperty(value = "Estado")
    private Boolean estado;
    @JsonProperty(value = "Movimiento")
    private Double movimiento;
    @JsonProperty(value = "Saldo Disponible")
    private Double saldoDisponible;
}

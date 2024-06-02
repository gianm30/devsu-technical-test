package com.gian.carrasco.reto.tecnico.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoRqVO {
    private Integer id;
    @NotNull
    @Size(min = 6, max = 20)
    private String nroCuenta;
    @JsonIgnore
    private Integer idCuenta;
    @NotNull
    @Min(0)
    private Integer idTipoMovimiento;
    @JsonIgnore
    private Double saldo;
    @NotNull
    private Double valor;
    @NotNull
    private LocalDate fecha;

    public MovimientoRqVO setIdCuenta(Integer idCuenta) {
        this.idCuenta = idCuenta;
        return this;
    }
}

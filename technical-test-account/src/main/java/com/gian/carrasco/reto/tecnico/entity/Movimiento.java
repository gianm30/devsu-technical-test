package com.gian.carrasco.reto.tecnico.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table
public class Movimiento {
    @Id
    private Integer id;
    private Integer idCuenta;
    private Integer idTipoMovimiento;
    private Double valor;
    private LocalDate fecha;
}

package com.gian.carrasco.reto.tecnico.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Persona {
    @Id
    private Integer id;
    private String nombre;
    private Integer idGenero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}

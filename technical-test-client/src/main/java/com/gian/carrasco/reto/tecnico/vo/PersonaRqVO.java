package com.gian.carrasco.reto.tecnico.vo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PersonaRqVO {
    private Integer id;
    @NotBlank
    @Size(min = 2, max = 100)
    private String nombre;
    @Min(value = 1)
    private Integer idGenero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}

package com.gian.carrasco.reto.tecnico.webclient.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PersonaRsVO {
    private Integer id;
    private String nombre;
    private GeneroRsVO genero;
    private Integer edad;
    private String identificacion;
    private String direccion;
    private String telefono;
}

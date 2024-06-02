package com.gian.carrasco.reto.tecnico.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class ClienteRsVO extends PersonaRsVO {
    private String contrasena;
    private Boolean estado;
}

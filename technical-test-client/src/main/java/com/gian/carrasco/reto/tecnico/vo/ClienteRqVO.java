package com.gian.carrasco.reto.tecnico.vo;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ClienteRqVO extends PersonaRqVO {
    @NotBlank
    @Size(min = 4, max = 20)
    private String contrasena;
    @NotNull
    private Boolean estado;
}

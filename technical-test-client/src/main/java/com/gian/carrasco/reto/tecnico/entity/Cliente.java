package com.gian.carrasco.reto.tecnico.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table
public class Cliente extends Persona {
    private String contrasena;
    private Boolean estado;

    public Cliente setEstado(Boolean estado) {
        this.estado = estado;
        return this;
    }
}

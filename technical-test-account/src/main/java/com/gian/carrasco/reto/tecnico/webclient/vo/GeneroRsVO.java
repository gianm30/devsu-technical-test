package com.gian.carrasco.reto.tecnico.webclient.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeneroRsVO {
    private Integer id;
    private String descripcion;
}

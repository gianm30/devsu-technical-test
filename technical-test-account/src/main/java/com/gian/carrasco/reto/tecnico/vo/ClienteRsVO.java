package com.gian.carrasco.reto.tecnico.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteRsVO {
    private Integer id;
    private String nombre;
}

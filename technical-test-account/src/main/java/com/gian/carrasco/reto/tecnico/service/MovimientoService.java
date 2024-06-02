package com.gian.carrasco.reto.tecnico.service;

import com.gian.carrasco.reto.tecnico.vo.MovimientoRqVO;
import com.gian.carrasco.reto.tecnico.vo.MovimientoRsVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface MovimientoService {
    Flux<MovimientoRsVO> findAll();

    Flux<MovimientoRsVO> findReport(Integer idCuenta, LocalDate desde, LocalDate hasta);

    Mono<MovimientoRsVO> create(MovimientoRqVO vo);

    Mono<MovimientoRsVO> update(MovimientoRqVO vo);

    Mono<MovimientoRsVO> patch(MovimientoRqVO vo);

    Mono<Void> delete(Integer id);
}

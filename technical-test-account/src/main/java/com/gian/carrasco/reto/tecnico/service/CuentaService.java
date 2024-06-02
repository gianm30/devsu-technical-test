package com.gian.carrasco.reto.tecnico.service;

import com.gian.carrasco.reto.tecnico.vo.CuentaRqVO;
import com.gian.carrasco.reto.tecnico.vo.CuentaRsVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaService {
    Flux<CuentaRsVO> findAll();

    Flux<CuentaRsVO> findByIdCliente(Integer idCliente);

    Mono<CuentaRsVO> findById(Integer id);

    Mono<CuentaRsVO> findByNumero(String numero);

    Mono<CuentaRsVO> create(CuentaRqVO vo);

    Mono<CuentaRsVO> update(CuentaRqVO vo);

    Mono<CuentaRsVO> patch(CuentaRqVO vo);

    Mono<Void> delete(Integer id);
}

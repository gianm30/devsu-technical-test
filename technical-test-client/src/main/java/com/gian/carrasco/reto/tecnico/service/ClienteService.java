package com.gian.carrasco.reto.tecnico.service;

import com.gian.carrasco.reto.tecnico.vo.ClienteRqVO;
import com.gian.carrasco.reto.tecnico.vo.ClienteRsVO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClienteService {
    Flux<ClienteRsVO> findAll();

    Mono<ClienteRsVO> findById(Integer id);

    Mono<ClienteRsVO> create(ClienteRqVO vo);

    Mono<ClienteRsVO> update(ClienteRqVO vo);

    Mono<ClienteRsVO> patch(ClienteRqVO vo);

    Mono<Void> delete(Integer id);
}

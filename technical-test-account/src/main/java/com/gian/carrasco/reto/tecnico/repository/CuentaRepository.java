package com.gian.carrasco.reto.tecnico.repository;

import com.gian.carrasco.reto.tecnico.entity.Cuenta;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CuentaRepository extends ReactiveCrudRepository<Cuenta, Integer> {
    Mono<Cuenta> findByNumero(String numero);
    Flux<Cuenta> findByIdCliente(Integer idCliente);
}

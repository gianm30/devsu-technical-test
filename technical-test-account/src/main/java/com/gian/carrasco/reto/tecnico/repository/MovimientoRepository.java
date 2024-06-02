package com.gian.carrasco.reto.tecnico.repository;

import com.gian.carrasco.reto.tecnico.entity.Movimiento;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface MovimientoRepository extends ReactiveCrudRepository<Movimiento, Integer> {
    Flux<Movimiento> findByIdCuentaAndFechaBetween(Integer idCuenta, LocalDate desde, LocalDate hasta);
}

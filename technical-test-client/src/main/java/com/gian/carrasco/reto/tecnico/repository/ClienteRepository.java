package com.gian.carrasco.reto.tecnico.repository;

import com.gian.carrasco.reto.tecnico.entity.Cliente;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ClienteRepository extends ReactiveCrudRepository<Cliente, Integer> {
}

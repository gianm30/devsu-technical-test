package com.gian.carrasco.reto.tecnico.service.impl;

import com.gian.carrasco.reto.tecnico.entity.Cliente;
import com.gian.carrasco.reto.tecnico.exception.IdNotExistsException;
import com.gian.carrasco.reto.tecnico.repository.ClienteRepository;
import com.gian.carrasco.reto.tecnico.repository.GeneroRepository;
import com.gian.carrasco.reto.tecnico.service.ClienteService;
import com.gian.carrasco.reto.tecnico.validators.NumbersValidator;
import com.gian.carrasco.reto.tecnico.vo.ClienteRqVO;
import com.gian.carrasco.reto.tecnico.vo.ClienteRsVO;
import com.gian.carrasco.reto.tecnico.vo.GeneroRsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class ClienteServiceImpl implements ClienteService {
    private final ClienteRepository repository;
    private final GeneroRepository generoRepository;

    public ClienteServiceImpl(@Autowired ClienteRepository repository, @Autowired GeneroRepository generoRepository) {
        this.repository = repository;
        this.generoRepository = generoRepository;
    }

    @Override
    public Flux<ClienteRsVO> findAll() {
        return repository.findAll()
                .flatMap(this::toResponse)
                .flatMap(this::loadGenero);
    }

    @Override
    public Mono<ClienteRsVO> findById(Integer id) {
        return repository.findById(id)
                .flatMap(this::toResponse)
                .switchIfEmpty(Mono.error(IdNotExistsException::new));
    }

    @Override
    public Mono<ClienteRsVO> create(ClienteRqVO vo) {
        return toEntity(vo)
                .flatMap(repository::save)
                .flatMap(this::toResponse)
                .flatMap(this::loadGenero);
    }

    @Override
    public Mono<ClienteRsVO> update(ClienteRqVO vo) {
        return repository.findById(vo.getId())
                .map(bd -> {
                    bd.setNombre(vo.getNombre());
                    bd.setIdGenero(vo.getIdGenero());
                    bd.setEdad(vo.getEdad());
                    bd.setIdentificacion(vo.getIdentificacion());
                    bd.setDireccion(vo.getDireccion());
                    bd.setTelefono(vo.getTelefono());
                    bd.setContrasena(vo.getContrasena());
                    bd.setEstado(vo.getEstado());
                    return bd;
                })
                .flatMap(repository::save)
                .flatMap(this::toResponse)
                .flatMap(this::loadGenero)
                .switchIfEmpty(Mono.error(IdNotExistsException::new));
    }

    @Override
    public Mono<ClienteRsVO> patch(ClienteRqVO vo) {
        return repository.findById(vo.getId())
                .map(bd -> {
                    Optional.ofNullable(vo.getNombre()).ifPresent(bd::setNombre);
                    Optional.ofNullable(vo.getIdGenero()).ifPresent(bd::setIdGenero);
                    Optional.ofNullable(vo.getEdad()).ifPresent(bd::setEdad);
                    Optional.ofNullable(vo.getIdentificacion()).ifPresent(bd::setIdentificacion);
                    Optional.ofNullable(vo.getDireccion()).ifPresent(bd::setDireccion);
                    Optional.ofNullable(vo.getTelefono()).ifPresent(bd::setTelefono);
                    Optional.ofNullable(vo.getContrasena()).ifPresent(bd::setContrasena);
                    Optional.ofNullable(vo.getEstado()).ifPresent(bd::setEstado);
                    return bd;
                })
                .flatMap(repository::save)
                .flatMap(this::toResponse)
                .flatMap(this::loadGenero)
                .switchIfEmpty(Mono.error(IdNotExistsException::new));
    }

    @Override
    public Mono<Void> delete(Integer id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(IdNotExistsException::new))
                .flatMap(repository::delete);
    }

    private Mono<ClienteRsVO> loadGenero(ClienteRsVO rs) {
        Optional<Integer> optional = Optional.ofNullable(rs.getGenero()).map(GeneroRsVO::getId);
        if(optional.isEmpty())
            return Mono.just(rs);

        return generoRepository.findById(optional.get()).map(x -> {
            rs.getGenero().setDescripcion(x.getDescripcion());
            return rs;
        });
    }

    private Mono<ClienteRsVO> toResponse(Cliente obj) {
        GeneroRsVO genero = Optional.ofNullable(obj.getIdGenero())
                .filter(NumbersValidator::isGreaterThanZero)
                .map(id -> GeneroRsVO.builder().id(id).build())
                .orElse(null);

        return Mono.just(ClienteRsVO.builder()
                .id(obj.getId())
                .nombre(obj.getNombre())
                .genero(genero)
                .edad(obj.getEdad())
                .identificacion(obj.getIdentificacion())
                .direccion(obj.getDireccion())
                .telefono(obj.getTelefono())
                .contrasena(obj.getContrasena())
                .estado(obj.getEstado())
                .build());
    }

    private Mono<Cliente> toEntity(ClienteRqVO obj) {
        return Mono.just(Cliente.builder()
                .id(obj.getId())
                .nombre(obj.getNombre())
                .idGenero(obj.getIdGenero())
                .edad(obj.getEdad())
                .identificacion(obj.getIdentificacion())
                .direccion(obj.getDireccion())
                .telefono(obj.getTelefono())
                .contrasena(obj.getContrasena())
                .estado(obj.getEstado())
                .build());
    }
}
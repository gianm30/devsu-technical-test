package com.gian.carrasco.reto.tecnico.service.impl;

import com.gian.carrasco.reto.tecnico.entity.Cliente;
import com.gian.carrasco.reto.tecnico.entity.Genero;
import com.gian.carrasco.reto.tecnico.repository.ClienteRepository;
import com.gian.carrasco.reto.tecnico.repository.GeneroRepository;
import com.gian.carrasco.reto.tecnico.service.ClienteService;
import com.gian.carrasco.reto.tecnico.vo.ClienteRqVO;
import com.gian.carrasco.reto.tecnico.vo.ClienteRsVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@Import(ClienteServiceImpl.class)
@SpringBootTest
class ClienteServiceImplTest {
    private static final int ID_GENERO_MASCULINO = 1;

    @MockBean
    private ClienteRepository repository;

    @MockBean
    private GeneroRepository generoRepository;

    @Autowired
    private ClienteService service;

    @Test
    void creation_when_ok() {
        ClienteRqVO rq = ClienteRqVO.builder()
                .nombre("Gian")
                .idGenero(ID_GENERO_MASCULINO)
                .edad(28)
                .identificacion("12345678")
                .direccion("Av. Mi casa con La del vecino")
                .telefono("555555")
                .contrasena("112233")
                .estado(true)
                .build();

        Mono<Cliente> entity = Mono.just(Cliente.builder()
                .nombre(rq.getNombre())
                .idGenero(rq.getIdGenero())
                .edad(rq.getEdad())
                .identificacion(rq.getIdentificacion())
                .direccion(rq.getDireccion())
                .telefono(rq.getTelefono())
                .contrasena(rq.getContrasena())
                .estado(rq.getEstado())
                .build());

        Mono<Genero> monoGenero = Mono.just(Genero.builder()
                .id(ID_GENERO_MASCULINO)
                .build());

        //GIVEN
        when(repository.save(any())).thenReturn(entity);
        when(generoRepository.findById(rq.getIdGenero())).thenReturn(monoGenero);

        //WHEN
        Mono<ClienteRsVO> mono = service.create(rq);

        //THEN
        StepVerifier.create(mono)
                .assertNext(rs -> {
                    assertEquals(rq.getNombre(), rs.getNombre());
                    assertEquals(rq.getIdGenero(), rs.getGenero().getId());
                    assertEquals(rq.getEdad(), rs.getEdad());
                    assertEquals(rq.getIdentificacion(), rs.getIdentificacion());
                    assertEquals(rq.getDireccion(), rs.getDireccion());
                    assertEquals(rq.getTelefono(), rs.getTelefono());
                    assertEquals(rq.getContrasena(), rs.getContrasena());
                    assertEquals(rq.getEstado(), rs.getEstado());
                })
                .verifyComplete();
        ;
    }
}
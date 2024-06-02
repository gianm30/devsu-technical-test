package com.gian.carrasco.reto.tecnico.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gian.carrasco.reto.tecnico.constant.Rutas;
import com.gian.carrasco.reto.tecnico.entity.Cliente;
import com.gian.carrasco.reto.tecnico.entity.Genero;
import com.gian.carrasco.reto.tecnico.errors.GlobalErrorWebExceptionHandler;
import com.gian.carrasco.reto.tecnico.errors.WebFluxConfig;
import com.gian.carrasco.reto.tecnico.repository.ClienteRepository;
import com.gian.carrasco.reto.tecnico.repository.GeneroRepository;
import com.gian.carrasco.reto.tecnico.service.ClienteService;
import com.gian.carrasco.reto.tecnico.service.impl.ClienteServiceImpl;
import com.gian.carrasco.reto.tecnico.validators.ValidationHandler;
import com.gian.carrasco.reto.tecnico.vo.ClienteRqVO;
import com.gian.carrasco.reto.tecnico.vo.ClienteRsVO;
import com.gian.carrasco.reto.tecnico.vo.ResponseVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({ClienteHandler.class, ClienteServiceImpl.class,
        GlobalErrorWebExceptionHandler.class, WebFluxConfig.class})
class ClienteHandlerTest {
    private static final int ID_GENERO_MASCULINO = 1;

    @MockBean
    private ClienteRepository repository;

    @MockBean
    private GeneroRepository generoRepository;

    @MockBean
    private ValidationHandler validationHandler;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void creation_when_ok() {
        ClienteRqVO rq = ClienteRqVO.builder()
                .nombre("Paco")
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
        when(validationHandler.validateBody(any())).thenReturn(Mono.just(rq));
        when(repository.save(any())).thenReturn(entity);
        when(generoRepository.findById(rq.getIdGenero())).thenReturn(monoGenero);


        //WHEN
        webTestClient.post().uri(Rutas.CLIENTES)
                .bodyValue(rq)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ResponseVO.class)
                //THEN
                .value(responseVO -> {
                    assertNotNull(responseVO);
                    assertNotNull(responseVO.getDatos());

                    ObjectMapper obj = new ObjectMapper();
                    ClienteRsVO rs = obj.convertValue(responseVO.getDatos(), ClienteRsVO.class);

                    assertNotNull(rs);
                    assertEquals(rq.getNombre(), rs.getNombre());
                    assertEquals(rq.getIdGenero(), rs.getGenero().getId());
                    assertEquals(rq.getEdad(), rs.getEdad());
                    assertEquals(rq.getIdentificacion(), rs.getIdentificacion());
                    assertEquals(rq.getDireccion(), rs.getDireccion());
                    assertEquals(rq.getTelefono(), rs.getTelefono());
                    assertEquals(rq.getContrasena(), rs.getContrasena());
                    assertEquals(rq.getEstado(), rs.getEstado());
                });
    }
}
package com.gian.carrasco.reto.tecnico.webclient.cliente;

import com.gian.carrasco.reto.tecnico.webclient.vo.ClienteRsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public class ClienteWebClient {
    private static final String API = "http://localhost:9090/clientes";
    private static final String API_ID = "http://localhost:9090/clientes/{id}";

    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<ClienteRsVO> findById(Integer id) {
        String uriString = UriComponentsBuilder.fromUriString(API_ID).buildAndExpand(id).toUriString();

        return webClientBuilder.build()
                .get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(ClienteRsVO.class);
    }

    public Mono<ClienteRsVO> update() {
        return webClientBuilder.build()
                .get()
                .uri(API)
                .retrieve()
                .bodyToMono(ClienteRsVO.class);
    }
}
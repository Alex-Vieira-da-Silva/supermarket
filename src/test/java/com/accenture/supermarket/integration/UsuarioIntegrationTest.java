package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.UsuarioDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    @Test
    void deveCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("123");
        dto.setRole("ADMIN");

        client.post()
                .uri("http://localhost:" + port + "/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo("alex");
    }

    @Test
    void deveListarUsuarios() {
        client.get()
                .uri("http://localhost:" + port + "/usuarios")
                .exchange()
                .expectStatus().isOk();
    }
}

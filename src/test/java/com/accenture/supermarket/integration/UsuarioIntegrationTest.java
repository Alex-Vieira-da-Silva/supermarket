package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.UsuarioDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class UsuarioIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Deve criar um usuário via API")
    void deveCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("123");
        dto.setRole("ADMIN");

        client.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.username").isEqualTo("alex")
                .jsonPath("$.role").isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Deve listar usuários via API")
    void deveListarUsuarios() {
        client.get()
                .uri("/usuarios")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }
}
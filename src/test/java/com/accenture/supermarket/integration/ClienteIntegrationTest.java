package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.ClienteDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ClienteIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Deve criar um cliente via API")
    void deveCriarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Alex");
        dto.setCpf("12345678901");
        dto.setTelefone("81999999999");
        dto.setEmail("alex@email");

        client.post()
                .uri("http://localhost:" + port + "/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nome").isEqualTo("Alex");
    }

    @Test
    @DisplayName("Deve listar clientes via API")
    void deveListarClientes() {
        client.get()
                .uri("http://localhost:" + port + "/clientes")
                .exchange()
                .expectStatus().isOk();
    }
}
package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.ProdutoDTO;
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
class ProdutoIntegrationTest {

    @Autowired
    private WebTestClient client;

    @Test
    @DisplayName("Deve criar um produto via API")
    void deveCriarProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Arroz");
        dto.setPreco(10.0);
        dto.setQuantidade(5);

        client.post()
                .uri("/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists()
                .jsonPath("$.nome").isEqualTo("Arroz")
                .jsonPath("$.preco").isEqualTo(10.0)
                .jsonPath("$.quantidade").isEqualTo(5);
    }

    @Test
    @DisplayName("Deve listar produtos via API")
    void deveListarProdutos() {
        client.get()
                .uri("/produtos")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$").isArray();
    }
}
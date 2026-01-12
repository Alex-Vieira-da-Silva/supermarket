package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.ProdutoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProdutoIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient client;

    @Test
    void deveCriarProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Arroz");
        dto.setPreco(10.0);
        dto.setQuantidade(5);

        client.post()
                .uri("http://localhost:" + port + "/produtos")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(dto)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.nome").isEqualTo("Arroz");
    }

    @Test
    void deveListarProdutos() {
        client.get()
                .uri("http://localhost:" + port + "/produtos")
                .exchange()
                .expectStatus().isOk();
    }
}

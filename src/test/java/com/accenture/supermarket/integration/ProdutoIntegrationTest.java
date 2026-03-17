package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProdutoIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Test
    @DisplayName("Deve criar um produto via API")
    void deveCriarProduto() {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Arroz");
        dto.setPreco(10.0);
        dto.setQuantidade(5);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProdutoDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<Produto> response =
                client.postForEntity("/produtos", request, Produto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Produto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getNome()).isEqualTo("Arroz");
        assertThat(body.getPreco()).isEqualTo(10.0);
        assertThat(body.getQuantidade()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve listar produtos via API")
    void deveListarProdutos() {
        ResponseEntity<List<Produto>> response = client.exchange(
                "/produtos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}

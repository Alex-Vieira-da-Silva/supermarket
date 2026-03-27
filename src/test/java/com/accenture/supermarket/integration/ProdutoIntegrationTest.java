package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.response.PageResponse;
import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.dto.response.ProdutoResponse;
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

        ResponseEntity<ProdutoResponse> response =
                client.postForEntity("/produtos", request, ProdutoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ProdutoResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.nome()).isEqualTo("Arroz");
        assertThat(body.preco()).isEqualTo(10.0);
        assertThat(body.quantidade()).isEqualTo(5);
    }

    @Test
    @DisplayName("Deve listar produtos via API com paginação")
    void deveListarProdutos() {
        ResponseEntity<PageResponse<ProdutoResponse>> response = client.exchange(
                "/produtos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<ProdutoResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).isNotNull();
    }
}

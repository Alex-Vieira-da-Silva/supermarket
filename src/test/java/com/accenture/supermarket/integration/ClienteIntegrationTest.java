package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.dto.response.ClienteResponse;
import com.accenture.supermarket.dto.response.PageResponse;
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
class ClienteIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Test
    @DisplayName("Deve criar um cliente via API")
    void deveCriarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Alex");
        dto.setCpf("123.456.789-01");
        dto.setTelefone("(81) 98812-3045");
        dto.setEmail("alex@email.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClienteDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<ClienteResponse> response =
                client.postForEntity("/clientes", request, ClienteResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ClienteResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.id()).isNotNull();
        assertThat(body.nome()).isEqualTo("Alex");
        assertThat(body.cpf()).isEqualTo("123.456.789-01");
    }

    @Test
    @DisplayName("Deve  listar clientes via API com paginação")
    void deveListarClientes() {
        ResponseEntity<PageResponse<ClienteResponse>> response = client.exchange(
                "/clientes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageResponse<ClienteResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        PageResponse<ClienteResponse> body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.content()).isNotNull();
    }
}

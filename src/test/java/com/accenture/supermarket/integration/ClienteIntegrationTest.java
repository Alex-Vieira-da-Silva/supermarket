package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.model.Cliente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;

import java.util.List;

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
        dto.setCpf("12345678901");
        dto.setTelefone("81999999999");
        dto.setEmail("alex@email");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ClienteDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<Cliente> response =
                client.postForEntity("/clientes", request, Cliente.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Cliente body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getNome()).isEqualTo("Alex");
        assertThat(body.getCpf()).isEqualTo("12345678901");
    }

    @Test
    @DisplayName("Deve  listar clientes via API")
    void deveListarClientes() {
        ResponseEntity<List<Cliente>> response = client.exchange(
                "/clientes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cliente>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<Cliente> body = response.getBody();
        assertThat(body).isNotNull();
    }
}

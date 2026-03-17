package com.accenture.supermarket.integration;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.model.Usuario;
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
class UsuarioIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Test
    @DisplayName("Deve criar um usuário via API")
    void deveCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("Senha@123");
        dto.setRole("ADMIN");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UsuarioDTO> request = new HttpEntity<>(dto, headers);

        ResponseEntity<Usuario> response =
                client.postForEntity("/usuarios", request, Usuario.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Usuario body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getId()).isNotNull();
        assertThat(body.getUsername()).isEqualTo("alex");
        assertThat(body.getRole()).isEqualTo("ADMIN");
    }

    @Test
    @DisplayName("Deve listar usuários via API")
    void deveListarUsuarios() {
        ResponseEntity<List<Usuario>> response = client.exchange(
                "/usuarios",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Usuario>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}

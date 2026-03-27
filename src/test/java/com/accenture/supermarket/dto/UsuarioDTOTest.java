package com.accenture.supermarket.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioDTOTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void tearDownValidator() {
        factory.close();
    }

    @Test
    void shouldFillFieldsWithSetters() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("Senha@123");
        dto.setRole("ADMIN");

        assertThat(dto.getUsername()).isEqualTo("alex");
        assertThat(dto.getPassword()).isEqualTo("Senha@123");
        assertThat(dto.getRole()).isEqualTo("ADMIN");
    }

    @Test
    void toStringShouldNotExposePassword() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("Senha@123");
        dto.setRole("ADMIN");

        String stringRepresentation = dto.toString();

        assertThat(stringRepresentation).contains("alex");
        assertThat(stringRepresentation).contains("ADMIN");
        assertThat(stringRepresentation).doesNotContain("Senha@123");
    }

    @Test
    void shouldDetectInvalidPasswordPattern() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("senha"); // não atende ao regex
        dto.setRole("ADMIN");

        assertThat(validator.validate(dto)).isNotEmpty();
    }
}

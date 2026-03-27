package com.accenture.supermarket.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteDTOTest {

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
    void allArgsConstructorShouldPopulateFields() {
        ClienteDTO dto = new ClienteDTO("Alex", "123.456.789-01", "(81) 98812-3045", "alex@email.com");

        assertThat(dto.getNome()).isEqualTo("Alex");
        assertThat(dto.getCpf()).isEqualTo("123.456.789-01");
        assertThat(dto.getTelefone()).isEqualTo("(81) 98812-3045");
        assertThat(dto.getEmail()).isEqualTo("alex@email.com");
    }

    @Test
    void shouldDetectInvalidCpf() {
        ClienteDTO dto = new ClienteDTO("Alex", "123", "(81) 98812-3045", "alex@email.com");

        assertThat(validator.validate(dto)).isNotEmpty();
    }

    @Test
    void shouldDetectInvalidEmail() {
        ClienteDTO dto = new ClienteDTO("Alex", "123.456.789-01", "(81) 98812-3045", "invalid-email");

        assertThat(validator.validate(dto)).isNotEmpty();
    }
}

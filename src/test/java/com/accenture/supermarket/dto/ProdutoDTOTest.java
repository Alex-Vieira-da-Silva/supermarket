package com.accenture.supermarket.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProdutoDTOTest {

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
        ProdutoDTO dto = new ProdutoDTO("Arroz", 10.0, 5);

        assertThat(dto.getNome()).isEqualTo("Arroz");
        assertThat(dto.getPreco()).isEqualTo(10.0);
        assertThat(dto.getQuantidade()).isEqualTo(5);
    }

    @Test
    void shouldDetectNegativePrice() {
        ProdutoDTO dto = new ProdutoDTO("Arroz", -1.0, 5);

        assertThat(validator.validate(dto)).isNotEmpty();
    }

    @Test
    void shouldDetectNegativeQuantity() {
        ProdutoDTO dto = new ProdutoDTO("Arroz", 10.0, -1);

        assertThat(validator.validate(dto)).isNotEmpty();
    }
}

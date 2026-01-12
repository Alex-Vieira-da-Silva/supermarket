package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Produto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @Test
    void deveSalvarProduto() {
        Produto produto = new Produto(null, "Arroz", 10.0, 5);

        Produto salvo = repository.save(produto);

        assertNotNull(salvo.getId());
        assertEquals("Arroz", salvo.getNome());
    }

    @Test
    void deveBuscarPorId() {
        Produto produto = new Produto(null, "Feijão", 8.0, 3);
        Produto salvo = repository.save(produto);

        Produto encontrado = repository.findById(salvo.getId()).orElse(null);

        assertNotNull(encontrado);
        assertEquals("Feijão", encontrado.getNome());
    }
}

package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository repository;

    @Test
    @DisplayName("Deve salvar um produto corretamente")
    void deveSalvarProduto() {
        Produto produto = new Produto(null, "Arroz", 10.0, 5);

        Produto salvo = repository.save(produto);

        assertAll(
                () -> assertNotNull(salvo.getId()),
                () -> assertEquals("Arroz", salvo.getNome()),
                () -> assertEquals(10.0, salvo.getPreco()),
                () -> assertEquals(5, salvo.getQuantidade())
        );
    }

    @Test
    @DisplayName("Deve buscar um produto pelo ID")
    void deveBuscarPorId() {
        Produto produto = new Produto(null, "Feijão", 8.0, 3);
        Produto salvo = repository.save(produto);

        Produto encontrado = repository.findById(salvo.getId()).orElse(null);

        assertAll(
                () -> assertNotNull(encontrado),
                () -> assertEquals("Feijão", encontrado.getNome()),
                () -> assertEquals(8.0, encontrado.getPreco()),
                () -> assertEquals(3, encontrado.getQuantidade())
        );
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar ID inexistente")
    void deveRetornarVazioQuandoIdNaoExiste() {
        Optional<Produto> resultado = repository.findById(999L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve deletar um produto pelo ID")
    void deveDeletarProduto() {
        Produto produto = new Produto(null, "Arroz", 10.0, 5);
        Produto salvo = repository.save(produto);

        repository.deleteById(salvo.getId());

        Optional<Produto> resultado = repository.findById(salvo.getId());

        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve atualizar um produto existente")
    void deveAtualizarProduto() {
        Produto produto = new Produto(null, "Arroz", 10.0, 5);
        Produto salvo = repository.save(produto);

        salvo.setNome("Arroz Integral");
        salvo.setPreco(12.0);

        Produto atualizado = repository.save(salvo);

        assertAll(
                () -> assertEquals("Arroz Integral", atualizado.getNome()),
                () -> assertEquals(12.0, atualizado.getPreco())
        );
    }

    @Test
    @DisplayName("Deve retornar true quando existsById encontrar o produto")
    void deveVerificarExistenciaPorId() {
        Produto produto = new Produto(null, "Arroz", 10.0, 5);
        Produto salvo = repository.save(produto);

        assertTrue(repository.existsById(salvo.getId()));
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodos() {
        repository.save(new Produto(null, "Arroz", 10.0, 5));

        List<Produto> lista = repository.findAll();

        assertFalse(lista.isEmpty());
    }
}
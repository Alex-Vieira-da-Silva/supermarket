package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(new Produto()));

        List<Produto> produtos = service.listarTodos();

        assertFalse(produtos.isEmpty());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar produto inexistente")
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve criar um produto com todos os campos")
    void deveCriarProduto() {
        ProdutoDTO dto = new ProdutoDTO("Arroz", 10.0, 5);
        Produto salvo = new Produto(1L, "Arroz", 10.0, 5);

        when(repository.save(any())).thenReturn(salvo);

        Produto resultado = service.criar(dto);

        assertAll(
                () -> assertEquals("Arroz", resultado.getNome()),
                () -> assertEquals(10.0, resultado.getPreco()),
                () -> assertEquals(5, resultado.getQuantidade()),
                () -> verify(repository).save(any(Produto.class))
        );
    }

    @Test
    @DisplayName("Deve atualizar um produto existente")
    void deveAtualizarProduto() {
        Produto existente = new Produto(1L, "Arroz", 10.0, 5);
        Produto salvo = new Produto(1L, "Feijão", 8.0, 3);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(salvo);

        ProdutoDTO dto = new ProdutoDTO("Feijão", 8.0, 3);

        Produto atualizado = service.atualizar(1L, dto);

        assertAll(
                () -> assertEquals("Feijão", atualizado.getNome()),
                () -> assertEquals(8.0, atualizado.getPreco()),
                () -> assertEquals(3, atualizado.getQuantidade()),
                () -> verify(repository).findById(1L),
                () -> verify(repository).save(existente)
        );
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar produto inexistente")
    void deveLancarErroAoAtualizarQuandoIdNaoExiste() {
        ProdutoDTO dto = new ProdutoDTO("Teste", 5.0, 2);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.atualizar(1L, dto));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar produto existente")
    void deveDeletarProduto() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar produto inexistente")
    void deveLancarErroAoDeletarQuandoIdNaoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deletar(1L));
        verify(repository).existsById(1L);
    }
}
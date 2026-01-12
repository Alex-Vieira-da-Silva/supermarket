package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
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
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(new Produto()));

        List<ProdutoDTO> produtos = service.listarTodos();

        assertFalse(produtos.isEmpty());
    }

    @Test
    void deveBuscarPorId() {
        Produto produto = new Produto(1L, "Arroz", 10.0, 5);
        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        ProdutoDTO dto = service.buscarPorId(1L);

        assertEquals("Arroz", dto.getNome());
    }

    @Test
    void deveCriarProduto() {
        ProdutoDTO dto = new ProdutoDTO(null, "Arroz", 10.0, 5);
        Produto salvo = new Produto(1L, "Arroz", 10.0, 5);

        when(repository.save(any())).thenReturn(salvo);

        ProdutoDTO resultado = service.criar(dto);

        assertEquals("Arroz", resultado.getNome());
    }

    @Test
    void deveAtualizarProduto() {
        Produto existente = new Produto(1L, "Arroz", 10.0, 5);
        Produto salvo = new Produto(1L, "Feijão", 8.0, 3);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(salvo);

        ProdutoDTO dto = new ProdutoDTO(null, "Feijão", 8.0, 3);

        ProdutoDTO atualizado = service.atualizar(1L, dto);

        assertEquals("Feijão", atualizado.getNome());
        assertEquals(8.0, atualizado.getPreco());
    }

    @Test
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(1L));
    }
}

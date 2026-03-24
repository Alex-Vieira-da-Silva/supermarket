package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.ProdutoNaoEncontradoException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.repository.ProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodos() {
        Page<Produto> page = new PageImpl<>(List.of(new Produto()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Produto> produtos = service.listar(null, Pageable.unpaged());

        assertFalse(produtos.isEmpty());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve filtrar produtos pelo nome")
    void deveFiltrarPorNome() {
        Page<Produto> page = new PageImpl<>(List.of(new Produto()));
        when(repository.findByNomeContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        service.listar("arroz", Pageable.unpaged());

        verify(repository).findByNomeContainingIgnoreCase("arroz", Pageable.unpaged());
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar produto inexistente")
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> service.buscarPorId(1L));
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

        assertThrows(ProdutoNaoEncontradoException.class, () -> service.atualizar(1L, dto));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar produto existente")
    void deveDeletarProduto() {
        Produto produto = new Produto(1L, "Arroz", 10.0, 5);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));
        doNothing().when(repository).delete(produto);

        service.deletar(1L);

        verify(repository).delete(produto);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar produto inexistente")
    void deveLancarErroAoDeletarQuandoIdNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProdutoNaoEncontradoException.class, () -> service.deletar(1L));
        verify(repository).findById(1L);
    }
}

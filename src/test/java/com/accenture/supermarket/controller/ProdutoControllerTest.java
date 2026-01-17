package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deve listar todos os produtos")
    void deveListarTodos() throws Exception {
        Mockito.when(service.listarTodos())
                .thenReturn(List.of(new Produto()));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve buscar produto por ID")
    void deveBuscarPorId() throws Exception {
        Produto dto = new Produto();
        dto.setId(1L);
        dto.setNome("Arroz");
        dto.setPreco(10.0);
        dto.setQuantidade(5);

        Mockito.when(service.buscarPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Arroz"))
                .andExpect(jsonPath("$.preco").value(10.0))
                .andExpect(jsonPath("$.quantidade").value(5));
    }

    @Test
    @DisplayName("Deve retornar 404 quando produto não for encontrado")
    void deveRetornar404QuandoNaoEncontrar() throws Exception {
        Mockito.when(service.buscarPorId(1L))
                .thenThrow(new NotFoundException("Produto não encontrado"));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Produto não encontrado"));
    }

    @Test
    @DisplayName("Deve criar um produto")
    void deveCriarProduto() throws Exception {
        Produto produto = new Produto();
        produto.setId(1L);
        produto.setNome("Arroz");
        produto.setPreco(10.0);
        produto.setQuantidade(5);

        Mockito.when(service.criar(any())).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(produto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/produtos/1"))
                .andExpect(jsonPath("$.nome").value("Arroz"));
    }

    @Test
    @DisplayName("Deve retornar erro de validação ao criar produto inválido")
    void deveRetornarErroDeValidacaoAoCriar() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(); // inválido

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve atualizar um produto")
    void deveAtualizarProduto() throws Exception {
        Produto dto = new Produto();
        dto.setNome("Feijão");
        dto.setPreco(8.0);
        dto.setQuantidade(3);

        Mockito.when(service.atualizar(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Feijão"));
    }

    @Test
    @DisplayName("Deve deletar um produto")
    void deveDeletarProduto() throws Exception {
        Mockito.doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }
}
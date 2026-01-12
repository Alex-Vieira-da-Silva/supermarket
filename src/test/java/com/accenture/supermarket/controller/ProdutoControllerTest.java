package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    void deveListarTodos() throws Exception {
        Mockito.when(service.listarTodos())
                .thenReturn(List.of(new ProdutoDTO()));

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
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
    void deveRetornar404QuandoNaoEncontrar() throws Exception {
        Mockito.when(service.buscarPorId(1L))
                .thenThrow(new NotFoundException("Produto não encontrado"));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Produto não encontrado"));
    }

    @Test
    void deveCriarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setNome("Arroz");
        dto.setPreco(10.0);
        dto.setQuantidade(5);

        Mockito.when(service.criar(any())).thenReturn(dto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Arroz"));
    }

    @Test
    void deveRetornarErroDeValidacaoAoCriar() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(); // inválido

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO();
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
    void deveDeletarProduto() throws Exception {
        Mockito.doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isOk());
    }
}

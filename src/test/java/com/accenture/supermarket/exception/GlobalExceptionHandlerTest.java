package com.accenture.supermarket.exception;

import com.accenture.supermarket.controller.ClienteController;
import com.accenture.supermarket.controller.ProdutoController;
import com.accenture.supermarket.controller.UsuarioController;
import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.service.ClienteService;
import com.accenture.supermarket.service.ProdutoService;
import com.accenture.supermarket.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {
        ClienteController.class,
        ProdutoController.class,
        UsuarioController.class
})
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private UsuarioService usuarioService;

    @Autowired
    private ObjectMapper mapper;

    // 400 - Validação
    @Test
    @DisplayName("Deve retornar 400 quando a validação falhar")
    void deveRetornar400QuandoValidacaoFalhar() throws Exception {
        ClienteDTO dto = new ClienteDTO(); // inválido

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").exists());
    }

    // 404 - Cliente
    @Test
    @DisplayName("Deve retornar 404 quando ClienteNaoEncontradoException for lançada")
    void deveRetornar404ClienteNaoEncontrado() throws Exception {
        Mockito.when(clienteService.buscarPorId(1L))
                .thenThrow(new ClienteNaoEncontradoException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Cliente não encontrado"));
    }

    // 404 - Produto
    @Test
    @DisplayName("Deve retornar 404 quando ProdutoNaoEncontradoException for lançada")
    void deveRetornar404ProdutoNaoEncontrado() throws Exception {
        Mockito.when(produtoService.buscarPorId(1L))
                .thenThrow(new ProdutoNaoEncontradoException("Produto não encontrado"));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Produto não encontrado"));
    }

    // 404 - Usuário
    @Test
    @DisplayName("Deve retornar 404 quando UsuarioNaoEncontradoException for lançada")
    void deveRetornar404UsuarioNaoEncontrado() throws Exception {
        Mockito.when(usuarioService.buscarPorId(1L))
                .thenThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Usuário não encontrado"));
    }

    // 500 - Erro genérico
    @Test
    @DisplayName("Deve retornar 500 quando ocorrer erro genérico")
    void deveRetornar500QuandoErroGenerico() throws Exception {
        Mockito.when(clienteService.listarTodos())
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.erro").value("Erro interno no servidor"));
    }
}
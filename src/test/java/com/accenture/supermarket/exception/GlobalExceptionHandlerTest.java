package com.accenture.supermarket.exception;

import com.accenture.supermarket.controller.ClienteController;
import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    @Autowired
    private ObjectMapper mapper;

    // 400 - Erros de validação
    @Test
    void deveRetornar400QuandoValidacaoFalhar() throws Exception {
        ClienteDTO dto = new ClienteDTO(); // inválido

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nome").exists());
    }

    // 404 - NotFoundException
    @Test
    void deveRetornar404QuandoNotFoundException() throws Exception {
        Mockito.when(service.buscarPorId(1L))
                .thenThrow(new NotFoundException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Cliente não encontrado"));
    }

    // 500 - Erro inesperado
    @Test
    void deveRetornar500QuandoErroGenerico() throws Exception {
        Mockito.when(service.listarTodos())
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.mensagem").value("Ocorreu um erro inesperado"));
    }
}

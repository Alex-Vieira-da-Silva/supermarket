package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deve criar um cliente")
    void deveCriarCliente() throws Exception {
        ClienteDTO dto = new ClienteDTO(
                "Alex",
                "12345678901",
                "81999999999",
                "alex@tomail.email"
        );

        Cliente cliente = new Cliente(
                1L,
                dto.getNome(),
                dto.getCpf(),
                dto.getTelefone(),
                dto.getEmail()
        );

        when(service.criar(any(ClienteDTO.class))).thenReturn(cliente);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/clientes/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nome").value("Alex"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.telefone").value("81999999999"))
                .andExpect(jsonPath("$.email").value("alex@email"));

        verify(service).criar(any(ClienteDTO.class));
    }
}
package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.service.UsuarioService;
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

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void deveListarTodos() throws Exception {
        Mockito.when(service.listarTodos()).thenReturn(List.of(new Usuario()));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk());
    }

    @Test
    void deveBuscarPorId() throws Exception {
        Usuario usuario = new Usuario(1L, "alex", "123", "ADMIN");

        Mockito.when(service.buscarPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("alex"));
    }

    @Test
    void deveRetornar404QuandoNaoEncontrar() throws Exception {
        Mockito.when(service.buscarPorId(1L))
                .thenThrow(new NotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensagem").value("Usuário não encontrado"));
    }

    @Test
    void deveCriarUsuario() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("123");
        dto.setRole("ADMIN");

        Mockito.when(service.criar(any())).thenReturn(new Usuario());

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornarErroDeValidacaoAoCriar() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(); // inválido

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("novo");
        dto.setPassword("321");
        dto.setRole("USER");

        Mockito.when(service.atualizar(eq(1L), any())).thenReturn(new Usuario());

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        Mockito.doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isOk());
    }
}

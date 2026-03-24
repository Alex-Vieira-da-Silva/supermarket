package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.security.JwtAuthenticationFilter;
import com.accenture.supermarket.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UsuarioController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodos() throws Exception {
        Usuario usuario = new Usuario(1L, "alex", "Senha@123", "ADMIN");
        Page<Usuario> page = new PageImpl<>(List.of(usuario));

        Mockito.when(service.listar(Mockito.nullable(String.class), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].username").value("alex"))
                .andExpect(jsonPath("$.content[0].role").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarPorId() throws Exception {
        Usuario usuario = new Usuario(1L, "alex", "Senha@123", "ADMIN");

        Mockito.when(service.buscarPorId(1L)).thenReturn(usuario);

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("alex"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando usuário não for encontrado")
    void deveRetornar404QuandoNaoEncontrar() throws Exception {
        Mockito.when(service.buscarPorId(1L))
                .thenThrow(new NotFoundException("Usuário não encontrado"));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve criar um usuário")
    void deveCriarUsuario() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("Senha@123");
        dto.setRole("ADMIN");

        Usuario usuarioCriado = new Usuario(1L, "alex", "Senha@123", "ADMIN");

        Mockito.when(service.criar(any(UsuarioDTO.class))).thenReturn(usuarioCriado);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/usuarios/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("alex"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve retornar erro de validação ao criar usuário inválido")
    void deveRetornarErroDeValidacaoAoCriar() throws Exception {
        UsuarioDTO dto = new UsuarioDTO(); // inválido

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha na validação dos dados"));
    }

    @Test
    @DisplayName("Deve atualizar um usuário")
    void deveAtualizarUsuario() throws Exception {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("novo");
        dto.setPassword("Senha@456");
        dto.setRole("USER");

        Usuario atualizado = new Usuario(1L, "novo", "Senha@456", "USER");

        Mockito.when(service.atualizar(eq(1L), any(UsuarioDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("novo"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("Deve deletar um usuário")
    void deveDeletarUsuario() throws Exception {
        Mockito.doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/usuarios/1"))
                .andExpect(status().isNoContent());
    }
}

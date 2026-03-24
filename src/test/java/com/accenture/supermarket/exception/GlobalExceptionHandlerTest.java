package com.accenture.supermarket.exception;

import com.accenture.supermarket.controller.ClienteController;
import com.accenture.supermarket.controller.ProdutoController;
import com.accenture.supermarket.controller.UsuarioController;
import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.security.JwtAuthenticationFilter;
import com.accenture.supermarket.service.ClienteService;
import com.accenture.supermarket.service.ProdutoService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        ClienteController.class,
        ProdutoController.class,
        UsuarioController.class
}, excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deve retornar 400 quando a validação falhar")
    void deveRetornar400QuandoValidacaoFalhar() throws Exception {
        ClienteDTO dto = new ClienteDTO(); // inválido

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha na validação dos dados"))
                .andExpect(jsonPath("$.details[0].field").value("nome"));
    }

    @Test
    @DisplayName("Deve retornar 404 quando ClienteNaoEncontradoException for lançada")
    void deveRetornar404ClienteNaoEncontrado() throws Exception {
        Mockito.when(clienteService.buscarPorId(1L))
                .thenThrow(new ClienteNaoEncontradoException("Cliente não encontrado"));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente não encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve retornar 404 quando ProdutoNaoEncontradoException for lançada")
    void deveRetornar404ProdutoNaoEncontrado() throws Exception {
        Mockito.when(produtoService.buscarPorId(1L))
                .thenThrow(new ProdutoNaoEncontradoException("Produto não encontrado"));

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Produto não encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve retornar 404 quando UsuarioNaoEncontradoException for lançada")
    void deveRetornar404UsuarioNaoEncontrado() throws Exception {
        Mockito.when(usuarioService.buscarPorId(1L))
                .thenThrow(new UsuarioNaoEncontradoException("Usuário não encontrado"));

        mockMvc.perform(get("/usuarios/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve retornar 401 quando a autenticação falhar")
    void deveRetornar401QuandoAutenticacaoFalhar() throws Exception {
        Mockito.when(clienteService.listar(Mockito.nullable(String.class), Mockito.nullable(String.class), Mockito.any(Pageable.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    @DisplayName("Deve retornar 500 quando ocorrer erro genérico")
    void deveRetornar500QuandoErroGenerico() throws Exception {
        Mockito.when(clienteService.listar(Mockito.nullable(String.class), Mockito.nullable(String.class), Mockito.any(Pageable.class)))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro interno no servidor"))
                .andExpect(jsonPath("$.status").value(500));
    }
}

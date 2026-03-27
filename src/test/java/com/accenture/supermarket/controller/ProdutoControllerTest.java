package com.accenture.supermarket.controller;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Produto;
import com.accenture.supermarket.security.JwtAuthenticationFilter;
import com.accenture.supermarket.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProdutoController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProdutoService service;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Deve listar todos os produtos com paginação")
    void deveListarTodos() throws Exception {
        Produto produto = new Produto(1L, "Arroz", 10.0, 5);
        Page<Produto> page = new PageImpl<>(List.of(produto));

        Mockito.when(service.listar(Mockito.nullable(String.class), any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].nome").value("Arroz"))
                .andExpect(jsonPath("$.content[0].preco").value(10.0))
                .andExpect(jsonPath("$.content[0].quantidade").value(5));
    }

    @Test
    @DisplayName("Deve buscar produto por ID")
    void deveBuscarPorId() throws Exception {
        Produto produto = new Produto(1L, "Arroz", 10.0, 5);

        Mockito.when(service.buscarPorId(1L)).thenReturn(produto);

        mockMvc.perform(get("/produtos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
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
                .andExpect(jsonPath("$.message").value("Produto não encontrado"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @DisplayName("Deve criar um produto")
    void deveCriarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO("Arroz", 10.0, 5);

        Produto produto = new Produto(1L, dto.getNome(), dto.getPreco(), dto.getQuantidade());

        Mockito.when(service.criar(any(ProdutoDTO.class))).thenReturn(produto);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/produtos/1"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Arroz"))
                .andExpect(jsonPath("$.preco").value(10.0))
                .andExpect(jsonPath("$.quantidade").value(5));
    }

    @Test
    @DisplayName("Deve retornar erro de validação ao criar produto inválido")
    void deveRetornarErroDeValidacaoAoCriar() throws Exception {
        ProdutoDTO dto = new ProdutoDTO(); // inválido

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Falha na validação dos dados"));
    }

    @Test
    @DisplayName("Deve atualizar um produto")
    void deveAtualizarProduto() throws Exception {
        ProdutoDTO dto = new ProdutoDTO("Feijão", 8.0, 3);

        Produto atualizado = new Produto(1L, dto.getNome(), dto.getPreco(), dto.getQuantidade());

        Mockito.when(service.atualizar(eq(1L), any(ProdutoDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/produtos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Feijão"))
                .andExpect(jsonPath("$.preco").value(8.0))
                .andExpect(jsonPath("$.quantidade").value(3));
    }

    @Test
    @DisplayName("Deve deletar um produto")
    void deveDeletarProduto() throws Exception {
        Mockito.doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/produtos/1"))
                .andExpect(status().isNoContent());
    }
}

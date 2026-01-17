package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(new Cliente()));

        List<Cliente> clientes = service.listarTodos();

        assertFalse(clientes.isEmpty());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve buscar cliente por ID")
    void deveBuscarPorId() {
        Cliente cliente = new Cliente(1L, "Alex", "12345678901", "81999999999", "alex@email");
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = service.buscarPorId(1L);

        assertAll(
                () -> assertEquals("Alex", resultado.getNome()),
                () -> verify(repository).findById(1L)
        );
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar cliente inexistente")
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve criar um cliente")
    void deveCriarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Alex");
        dto.setCpf("12345678901");
        dto.setTelefone("81999999999");
        dto.setEmail("alex@email");

        Cliente salvo = new Cliente(1L, dto.getNome(), dto.getCpf(), dto.getTelefone(), dto.getEmail());
        when(repository.save(any())).thenReturn(salvo);

        Cliente resultado = service.criar(dto);

        assertAll(
                () -> assertEquals("Alex", resultado.getNome()),
                () -> assertEquals("12345678901", resultado.getCpf()),
                () -> assertEquals("81999999999", resultado.getTelefone()),
                () -> assertEquals("alex@email", resultado.getEmail()),
                () -> verify(repository).save(any(Cliente.class))
        );
    }

    @Test
    @DisplayName("Deve atualizar um cliente existente")
    void deveAtualizarCliente() {
        Cliente existente = new Cliente(1L, "Alex", "12345678901", "81999999999", "alex@email");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Novo Nome");
        dto.setCpf("12345678901");
        dto.setTelefone("81999999999");
        dto.setEmail("novo@email");

        Cliente atualizado = service.atualizar(1L, dto);

        assertAll(
                () -> assertEquals("Novo Nome", atualizado.getNome()),
                () -> assertEquals("12345678901", atualizado.getCpf()),
                () -> assertEquals("81999999999", atualizado.getTelefone()),
                () -> assertEquals("novo@email", atualizado.getEmail()),
                () -> verify(repository).findById(1L),
                () -> verify(repository).save(existente)
        );
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar atualizar cliente inexistente")
    void deveLancarErroAoAtualizarQuandoIdNaoExiste() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Teste");
        dto.setCpf("12345678901");
        dto.setTelefone("81999999999");
        dto.setEmail("teste@email");

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.atualizar(1L, dto));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar cliente existente")
    void deveDeletarCliente() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar cliente inexistente")
    void deveLancarErroAoDeletarQuandoIdNaoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> service.deletar(1L));
        verify(repository).existsById(1L);
    }
}
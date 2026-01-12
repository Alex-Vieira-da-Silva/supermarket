package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
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
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(new Cliente()));

        List<Cliente> clientes = service.listarTodos();

        assertFalse(clientes.isEmpty());
    }

    @Test
    void deveBuscarPorId() {
        Cliente cliente = new Cliente(1L, "Alex", "12345678901", "81999999999", "alex@email");
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente resultado = service.buscarPorId(1L);

        assertEquals("Alex", resultado.getNome());
    }

    @Test
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(1L));
    }

    @Test
    void deveCriarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNome("Alex");
        dto.setCpf("12345678901");
        dto.setTelefone("81999999999");
        dto.setEmail("alex@email");

        Cliente salvo = new Cliente(1L, dto.getNome(), dto.getCpf(), dto.getTelefone(), dto.getEmail());
        when(repository.save(any())).thenReturn(salvo);

        Cliente resultado = service.criar(dto);

        assertEquals("Alex", resultado.getNome());
    }

    @Test
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

        assertEquals("Novo Nome", atualizado.getNome());
    }

    @Test
    void deveDeletarCliente() {
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}

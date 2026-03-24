package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.exception.ClienteNaoEncontradoException;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    @DisplayName("Deve listar todos os clientes")
    void deveListarTodos() {
        Page<Cliente> page = new PageImpl<>(List.of(new Cliente()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Cliente> clientes = service.listar(null, null, Pageable.unpaged());

        assertFalse(clientes.isEmpty());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve filtrar clientes por nome")
    void deveFiltrarPorNome() {
        Page<Cliente> page = new PageImpl<>(List.of(new Cliente()));
        when(repository.findByNomeContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        service.listar("alex", null, Pageable.unpaged());

        verify(repository).findByNomeContainingIgnoreCase("alex", Pageable.unpaged());
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

        assertThrows(ClienteNaoEncontradoException.class, () -> service.buscarPorId(1L));
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

        Cliente salvo = new Cliente(1L, dto.getNome(), "123.456.789-01", "(81)99999-9999", dto.getEmail());
        when(repository.save(any())).thenReturn(salvo);

        Cliente resultado = service.criar(dto);

        assertAll(
                () -> assertEquals("Alex", resultado.getNome()),
                () -> assertEquals("123.456.789-01", resultado.getCpf()),
                () -> assertEquals("(81)99999-9999", resultado.getTelefone()),
                () -> assertEquals("alex@email", resultado.getEmail()),
                () -> verify(repository).save(any(Cliente.class))
        );
    }

    @Test
    @DisplayName("Deve atualizar um cliente existente")
    void deveAtualizarCliente() {
        Cliente existente = new Cliente(1L, "Alex", "12345678901", "(81)99999-9999", "alex@email");
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
                () -> assertEquals("123.456.789-01", atualizado.getCpf()),
                () -> assertEquals("(81)99999-9999", atualizado.getTelefone()),
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

        assertThrows(ClienteNaoEncontradoException.class, () -> service.atualizar(1L, dto));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve deletar cliente existente")
    void deveDeletarCliente() {
        Cliente cliente = new Cliente(1L, "Alex", "12345678901", "81999999999", "alex@email");

        when(repository.findById(1L)).thenReturn(Optional.of(cliente));
        doNothing().when(repository).delete(cliente);

        service.deletar(1L);

        verify(repository).delete(cliente);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar cliente inexistente")
    void deveLancarErroAoDeletarQuandoIdNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ClienteNaoEncontradoException.class, () -> service.deletar(1L));
        verify(repository).findById(1L);
    }
}

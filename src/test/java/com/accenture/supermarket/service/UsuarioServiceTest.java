package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.exception.UsuarioNaoEncontradoException;
import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;


    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(new Usuario()));

        List<Usuario> usuarios = service.listarTodos();

        assertFalse(usuarios.isEmpty());
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Deve buscar usuário por ID")
    void deveBuscarPorId() {
        Usuario usuario = new Usuario(1L, "alex", "123", "ADMIN");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.buscarPorId(1L);

        assertAll(
                () -> assertEquals("alex", resultado.getUsername()),
                () -> assertEquals("ADMIN", resultado.getRole()),
                () -> verify(repository).findById(1L)
        );
    }

    @Test
    @DisplayName("Deve lançar erro ao buscar usuário inexistente")
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> service.buscarPorId(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Deve criar um usuário")
    void deveCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("123");
        dto.setRole("ADMIN");

        Usuario salvo = new Usuario(1L, "alex", "123", "ADMIN");
        when(repository.save(any())).thenReturn(salvo);

        Usuario resultado = service.criar(dto);

        assertAll(
                () -> assertEquals("alex", resultado.getUsername()),
                () -> assertEquals("ADMIN", resultado.getRole()),
                () -> verify(repository).save(any(Usuario.class))
        );
    }

    @Test
    @DisplayName("Deve atualizar um usuário existente")
    void deveAtualizarUsuario() {
        Usuario existente = new Usuario(1L, "alex", "123", "ADMIN");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("novo");
        dto.setPassword("321");
        dto.setRole("USER");

        Usuario atualizado = service.atualizar(1L, dto);

        assertAll(
                () -> assertEquals("novo", atualizado.getUsername()),
                () -> assertEquals("USER", atualizado.getRole()),
                () -> verify(repository).findById(1L),
                () -> verify(repository).save(existente)
        );
    }

    @Test
    @DisplayName("Deve deletar um usuário existente")
    void deveDeletarUsuario() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar usuário inexistente")
    void deveLancarErroAoDeletarQuandoIdNaoExiste() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(UsuarioNaoEncontradoException.class, () -> service.deletar(1L));
        verify(repository).existsById(1L);
    }
}
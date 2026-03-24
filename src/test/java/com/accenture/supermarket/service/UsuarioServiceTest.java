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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService service;

    @Test
    @DisplayName("Deve listar todos os usuários")
    void deveListarTodos() {
        Page<Usuario> page = new PageImpl<>(List.of(new Usuario()));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Usuario> usuarios = service.listar(null, Pageable.unpaged());

        assertFalse(usuarios.isEmpty());
        verify(repository).findAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Deve filtrar usuários por username")
    void deveFiltrarPorUsername() {
        Page<Usuario> page = new PageImpl<>(List.of(new Usuario()));
        when(repository.findByUsernameContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(page);

        service.listar("alex", Pageable.unpaged());

        verify(repository).findByUsernameContainingIgnoreCase("alex", Pageable.unpaged());
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
        dto.setPassword("Senha@123");
        dto.setRole("ADMIN");

        when(passwordEncoder.encode(any())).thenReturn("hash");

        Usuario salvo = new Usuario(1L, "alex", "hash", "ADMIN");
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
        Usuario existente = new Usuario(1L, "alex", "hash", "ADMIN");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("novo");
        dto.setPassword("Senha@321");
        dto.setRole("USER");

        when(passwordEncoder.encode(any())).thenReturn("hash2");

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
        Usuario usuario = new Usuario(1L, "alex", "123", "ADMIN");

        when(repository.findById(1L)).thenReturn(Optional.of(usuario));
        doNothing().when(repository).delete(usuario);

        service.deletar(1L);

        verify(repository).delete(usuario);
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar deletar usuário inexistente")
    void deveLancarErroAoDeletarQuandoIdNaoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class, () -> service.deletar(1L));
        verify(repository).findById(1L);
    }
}

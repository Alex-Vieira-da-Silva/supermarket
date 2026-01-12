package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.exception.NotFoundException;
import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveListarTodos() {
        when(repository.findAll()).thenReturn(List.of(new Usuario()));

        List<Usuario> usuarios = service.listarTodos();

        assertFalse(usuarios.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void deveBuscarPorId() {
        Usuario usuario = new Usuario(1L, "alex", "123", "ADMIN");
        when(repository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = service.buscarPorId(1L);

        assertEquals("alex", resultado.getUsername());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveLancarErroQuandoNaoEncontrar() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.buscarPorId(1L));
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void deveCriarUsuario() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("alex");
        dto.setPassword("123");
        dto.setRole("ADMIN");

        Usuario salvo = new Usuario(1L, "alex", "123", "ADMIN");
        when(repository.save(any())).thenReturn(salvo);

        Usuario resultado = service.criar(dto);

        assertEquals("alex", resultado.getUsername());
        verify(repository, times(1)).save(any());
    }

    @Test
    void deveAtualizarUsuario() {
        Usuario existente = new Usuario(1L, "alex", "123", "ADMIN");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername("novo");
        dto.setPassword("321");
        dto.setRole("USER");

        Usuario atualizado = service.atualizar(1L, dto);

        assertEquals("novo", atualizado.getUsername());
        assertEquals("USER", atualizado.getRole());
        verify(repository, times(1)).save(existente);
    }

    @Test
    void deveDeletarUsuario() {
        doNothing().when(repository).deleteById(1L);

        service.deletar(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}

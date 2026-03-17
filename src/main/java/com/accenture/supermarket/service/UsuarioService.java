package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.exception.UsuarioNaoEncontradoException;
import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id));
    }

    public Usuario criar(UsuarioDTO dto) {
        Usuario usuario = Usuario.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();

        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        usuario.atualizar(dto, passwordEncoder);
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new UsuarioNaoEncontradoException("Usuário não encontrado com ID: " + id);
        }

        repository.deleteById(id);
    }
}

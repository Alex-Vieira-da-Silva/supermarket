package com.accenture.supermarket.service;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.exception.DuplicateResourceException;
import com.accenture.supermarket.exception.UsuarioNaoEncontradoException;
import com.accenture.supermarket.mapper.UsuarioMapper;
import com.accenture.supermarket.model.Usuario;
import com.accenture.supermarket.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public Page<Usuario> listar(String username, Pageable pageable) {
        if (StringUtils.hasText(username)) {
            return repository.findByUsernameContainingIgnoreCase(username, pageable);
        }
        return repository.findAll(pageable);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    public Usuario criar(UsuarioDTO dto) {
        if (repository.existsByUsername(dto.getUsername())) {
            throw new DuplicateResourceException("Username já cadastrado");
        }
        return repository.save(UsuarioMapper.toEntity(dto, passwordEncoder));
    }

    public Usuario atualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarPorId(id);
        if (repository.existsByUsernameAndIdNot(dto.getUsername(), id)) {
            throw new DuplicateResourceException("Username já cadastrado");
        }
        UsuarioMapper.updateEntity(usuario, dto, passwordEncoder);
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        repository.delete(usuario);
    }
}

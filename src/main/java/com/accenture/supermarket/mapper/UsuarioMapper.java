package com.accenture.supermarket.mapper;

import com.accenture.supermarket.dto.UsuarioDTO;
import com.accenture.supermarket.model.Usuario;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioDTO dto, PasswordEncoder encoder) {
        return Usuario.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
    }

    public static void updateEntity(Usuario entity, UsuarioDTO dto, PasswordEncoder encoder) {
        entity.setUsername(dto.getUsername());
        entity.setPassword(encoder.encode(dto.getPassword()));
        entity.setRole(dto.getRole());
    }
}

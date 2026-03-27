package com.accenture.supermarket.dto.response;

import com.accenture.supermarket.model.Usuario;

public record UsuarioResponse(
        Long id,
        String username,
        String role
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole()
        );
    }
}

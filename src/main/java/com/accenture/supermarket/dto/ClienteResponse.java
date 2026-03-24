package com.accenture.supermarket.dto;

import com.accenture.supermarket.model.Cliente;

public record ClienteResponse(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String email
) {
    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEmail()
        );
    }
}

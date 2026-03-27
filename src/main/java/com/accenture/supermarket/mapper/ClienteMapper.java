package com.accenture.supermarket.mapper;

import com.accenture.supermarket.dto.ClienteDTO;
import com.accenture.supermarket.model.Cliente;
import com.accenture.supermarket.util.CpfUtils;
import com.accenture.supermarket.util.PhoneUtils;

public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteDTO dto) {
        return Cliente.builder()
                .nome(dto.getNome())
                .cpf(CpfUtils.format(dto.getCpf()))
                .telefone(PhoneUtils.format(dto.getTelefone()))
                .email(dto.getEmail())
                .build();
    }

    public static void updateEntity(Cliente entity, ClienteDTO dto) {
        entity.setNome(dto.getNome());
        entity.setCpf(CpfUtils.format(dto.getCpf()));
        entity.setTelefone(PhoneUtils.format(dto.getTelefone()));
        entity.setEmail(dto.getEmail());
    }
}

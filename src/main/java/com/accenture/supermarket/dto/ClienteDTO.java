package com.accenture.supermarket.dto;

import com.accenture.supermarket.model.Cliente;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String cpf;

    private String telefone;

    private String email;

    public ClienteDTO() {}

    public ClienteDTO(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }
}
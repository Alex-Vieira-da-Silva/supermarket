package com.accenture.supermarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
    private String nome;

    @Pattern(
            regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})?$",
            message = "CPF deve estar no formato 000.000.000-00 ou conter 11 dígitos"
    )
    private String cpf;

    @Pattern(
            regexp = "^(\\+?\\d{10,15}|\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4})?$",
            message = "Telefone deve estar no formato (DD) 00000-0000 ou conter de 10 a 15 dígitos"
    )
    private String telefone;

    @Email(message = "Email inválido")
    private String email;

    public ClienteDTO() {}

    public ClienteDTO(String nome, String cpf, String telefone, String email) {
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.email = email;
    }
}

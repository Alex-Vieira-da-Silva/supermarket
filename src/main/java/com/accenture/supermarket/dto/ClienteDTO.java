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

    @Pattern(regexp = "^(\\d{11})?$", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @Pattern(regexp = "^(\\+?\\d{10,15})?$", message = "Telefone deve conter apenas dígitos e opcional +, entre 10 e 15 caracteres")
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

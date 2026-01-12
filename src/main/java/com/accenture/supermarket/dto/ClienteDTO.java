package com.accenture.supermarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ClienteDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String cpf;

    private String telefone;

    private String email;
}

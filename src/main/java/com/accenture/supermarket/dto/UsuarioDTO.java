package com.accenture.supermarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
public class UsuarioDTO {

    @NotBlank(message = "Username é obrigatório")
    private String username;

    @NotBlank(message = "Password é obrigatório")
    @ToString.Exclude
    private String password;

    @NotBlank(message = "Role é obrigatória")
    private String role;

    public UsuarioDTO() {}
}
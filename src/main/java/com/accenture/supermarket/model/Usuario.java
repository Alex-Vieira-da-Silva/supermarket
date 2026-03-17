package com.accenture.supermarket.model;

import com.accenture.supermarket.dto.UsuarioDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String role; // ADMIN, USER, etc.

    public void atualizar(UsuarioDTO dto, PasswordEncoder passwordEncoder) {
        this.username = dto.getUsername();
        this.password = passwordEncoder.encode(dto.getPassword());
        this.role = dto.getRole();
    }

}

package com.accenture.supermarket.model;

import com.accenture.supermarket.dto.ClienteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome e obrigatorio")
    @Size(max = 120, message = "Nome deve ter no maximo 120 caracteres")
    private String nome;

    @Pattern(
            regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})?$",
            message = "CPF deve estar no formato 000.000.000-00 ou conter 11 digitos"
    )
    private String cpf;

    @Pattern(
            regexp = "^(\\+?\\d{10,15}|\\(\\d{2}\\)\\s?\\d{4,5}-\\d{4})?$",
            message = "Telefone deve estar no formato (DD)00000-0000 ou conter de 10 a 15 dígitos"
    )
    private String telefone;

    @Email(message = "Email invalido")
    private String email;

    @PrePersist
    @PreUpdate
    private void aplicarFormatacoes() {
        this.cpf = formatCpf(this.cpf);
        this.telefone = formatTelefone(this.telefone);
    }

    private String formatCpf(String cpf) {
        if (!StringUtils.hasText(cpf)) {
            return cpf;
        }
        String digits = cpf.replaceAll("\\D", "");
        if (digits.length() != 11) {
            return cpf.trim();
        }
        return String.format("%s.%s.%s-%s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 9),
                digits.substring(9));
    }

    private String formatTelefone(String telefone) {
        if (!StringUtils.hasText(telefone)) {
            return telefone;
        }
        String digits = telefone.replaceAll("\\D", "");
        if (digits.length() != 11) {
            return telefone.trim();
        }
        return String.format("(%s)%s-%s",
                digits.substring(0, 2),
                digits.substring(2, 7),
                digits.substring(7));
    }

    public void atualizar(ClienteDTO dto) {
        this.nome = dto.getNome();
        this.cpf = dto.getCpf();
        this.telefone = dto.getTelefone();
        this.email = dto.getEmail();
    }
}

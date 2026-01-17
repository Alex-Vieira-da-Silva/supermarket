package com.accenture.supermarket.model;

import com.accenture.supermarket.dto.ClienteDTO;
import jakarta.persistence.*;
import lombok.*;

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

    private String nome;

    private String cpf;

    private String telefone;

    private String email;

    public void atualizar(ClienteDTO dto) {
        this.nome = dto.getNome();
        this.cpf = dto.getCpf();
        this.telefone = dto.getTelefone();
        this.email = dto.getEmail();
    }

}
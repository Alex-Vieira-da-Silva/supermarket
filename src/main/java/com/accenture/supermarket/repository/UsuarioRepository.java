package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

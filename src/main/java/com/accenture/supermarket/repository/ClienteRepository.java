package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}

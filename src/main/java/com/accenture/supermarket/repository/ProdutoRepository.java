package com.accenture.supermarket.repository;

import com.accenture.supermarket.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
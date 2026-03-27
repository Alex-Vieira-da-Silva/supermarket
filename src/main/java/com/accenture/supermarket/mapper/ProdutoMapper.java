package com.accenture.supermarket.mapper;

import com.accenture.supermarket.dto.ProdutoDTO;
import com.accenture.supermarket.model.Produto;

public final class ProdutoMapper {

    private ProdutoMapper() {
    }

    public static Produto toEntity(ProdutoDTO dto) {
        return Produto.builder()
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .quantidade(dto.getQuantidade())
                .build();
    }

    public static void updateEntity(Produto entity, ProdutoDTO dto) {
        entity.setNome(dto.getNome());
        entity.setPreco(dto.getPreco());
        entity.setQuantidade(dto.getQuantidade());
    }
}

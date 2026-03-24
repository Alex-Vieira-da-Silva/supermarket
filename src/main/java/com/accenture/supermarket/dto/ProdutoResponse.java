package com.accenture.supermarket.dto;

import com.accenture.supermarket.model.Produto;

public record ProdutoResponse(
        Long id,
        String nome,
        Double preco,
        Integer quantidade
) {
    public static ProdutoResponse from(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getPreco(),
                produto.getQuantidade()
        );
    }
}

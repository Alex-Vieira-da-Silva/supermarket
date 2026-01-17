package com.accenture.supermarket.exception;

public class ProdutoNaoEncontradoException extends RuntimeException {

    public ProdutoNaoEncontradoException(Long id) {
        super("Produto não encontrado: " + id);
    }

    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }
}
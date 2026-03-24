package com.accenture.supermarket.exception;

public class ClienteNaoEncontradoException extends NotFoundException {

    public ClienteNaoEncontradoException(Long id) {
        super("Cliente não encontrado: " + id);
    }

    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}

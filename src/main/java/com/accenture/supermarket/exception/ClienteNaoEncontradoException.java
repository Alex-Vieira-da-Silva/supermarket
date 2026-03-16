package com.accenture.supermarket.exception;

public class ClienteNaoEncontradoException extends NotFoundException {

    public ClienteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
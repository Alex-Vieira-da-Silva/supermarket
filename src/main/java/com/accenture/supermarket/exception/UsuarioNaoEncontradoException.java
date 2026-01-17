package com.accenture.supermarket.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado: " + id);
    }

    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}
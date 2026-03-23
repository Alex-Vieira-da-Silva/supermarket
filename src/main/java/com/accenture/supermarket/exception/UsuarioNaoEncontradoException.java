package com.accenture.supermarket.exception;

public class UsuarioNaoEncontradoException extends NotFoundException {

    public UsuarioNaoEncontradoException(Long id) {
        super("Usuário não encontrado: " + id);
    }

    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }
}

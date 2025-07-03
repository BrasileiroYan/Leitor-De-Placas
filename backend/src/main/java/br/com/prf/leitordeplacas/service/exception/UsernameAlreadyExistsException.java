package br.com.prf.leitordeplacas.service.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String username) {
        super("O e-mail [" + username + "] já está em uso.");
    }
}

package br.com.prf.leitordeplacas.service.exception.authentication;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}

package br.com.prf.leitordeplacas.service.exception.authentication;

public class InvalidPasswordResetTokenException extends RuntimeException {
    public InvalidPasswordResetTokenException(String message) {
        super(message);
    }
}

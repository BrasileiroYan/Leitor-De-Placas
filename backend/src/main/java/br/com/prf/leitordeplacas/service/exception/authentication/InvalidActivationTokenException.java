package br.com.prf.leitordeplacas.service.exception.authentication;

public class InvalidActivationTokenException extends RuntimeException {
    public InvalidActivationTokenException(String message) {
        super(message);
    }
}

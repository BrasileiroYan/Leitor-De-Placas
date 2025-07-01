package br.com.prf.leitordeplacas.service.exception.authentication;

public class SamePasswordException extends RuntimeException {
    public SamePasswordException(String message) {
        super(message);
    }
}

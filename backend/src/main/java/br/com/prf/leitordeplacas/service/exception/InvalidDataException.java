package br.com.prf.leitordeplacas.service.exception;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}

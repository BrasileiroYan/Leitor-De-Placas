package br.com.prf.leitordeplacas.service.exception;

public class OcrProcessingException extends RuntimeException {
    public OcrProcessingException(String message) {
        super(message);
    }
}

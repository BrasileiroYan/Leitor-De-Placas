package br.com.prf.leitordeplacas.service.exception.authentication;

public class AppUserAlreadyEnabledException extends RuntimeException {
    public AppUserAlreadyEnabledException(String message) {
        super(message);
    }
}

package br.com.prf.leitordeplacas.service.exception.authentication;

public class AppUserAlreadyDisabledException extends RuntimeException {
    public AppUserAlreadyDisabledException(String message) {
        super(message);
    }
}

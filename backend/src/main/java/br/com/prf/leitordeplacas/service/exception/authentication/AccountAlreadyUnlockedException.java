package br.com.prf.leitordeplacas.service.exception.authentication;

public class AccountAlreadyUnlockedException extends RuntimeException {
    public AccountAlreadyUnlockedException(String message) {
        super(message);
    }
}

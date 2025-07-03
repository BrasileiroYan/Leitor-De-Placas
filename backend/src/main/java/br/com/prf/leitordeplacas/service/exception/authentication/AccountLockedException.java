package br.com.prf.leitordeplacas.service.exception.authentication;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException(String message) {
        super(message);
    }
}

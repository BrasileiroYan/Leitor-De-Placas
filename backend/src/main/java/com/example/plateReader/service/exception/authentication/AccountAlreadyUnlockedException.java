package com.example.plateReader.service.exception.authentication;

public class AccountAlreadyUnlockedException extends RuntimeException {
    public AccountAlreadyUnlockedException(String message) {
        super(message);
    }
}

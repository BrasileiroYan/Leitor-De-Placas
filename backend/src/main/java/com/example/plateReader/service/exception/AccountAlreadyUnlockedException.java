package com.example.plateReader.service.exception;

public class AccountAlreadyUnlockedException extends RuntimeException {
    public AccountAlreadyUnlockedException(String message) {
        super(message);
    }
}

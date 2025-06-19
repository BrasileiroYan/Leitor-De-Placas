package com.example.plateReader.service.exception;

public class AppUserAlreadyDisabledException extends RuntimeException {
    public AppUserAlreadyDisabledException(String message) {
        super(message);
    }
}

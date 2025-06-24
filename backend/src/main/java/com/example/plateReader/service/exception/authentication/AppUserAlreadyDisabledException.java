package com.example.plateReader.service.exception.authentication;

public class AppUserAlreadyDisabledException extends RuntimeException {
    public AppUserAlreadyDisabledException(String message) {
        super(message);
    }
}

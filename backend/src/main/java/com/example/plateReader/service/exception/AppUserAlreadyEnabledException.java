package com.example.plateReader.service.exception;

public class AppUserAlreadyEnabledException extends RuntimeException {
    public AppUserAlreadyEnabledException(String message) {
        super(message);
    }
}

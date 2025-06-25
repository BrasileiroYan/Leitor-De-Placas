package com.example.plateReader.service.exception.authentication;

public class RefreshTokenException extends RuntimeException {
    public RefreshTokenException(String message) {
        super(message);
    }

    public RefreshTokenException(String token, String message) {
        super(message);
    }
}

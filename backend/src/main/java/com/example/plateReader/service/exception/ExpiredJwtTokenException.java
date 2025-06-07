package com.example.plateReader.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ExpiredJwtTokenException extends JwtAuthenticationException {
    public ExpiredJwtTokenException(String message) {
        super(message);
    }

  public ExpiredJwtTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}

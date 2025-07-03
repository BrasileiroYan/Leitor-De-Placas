package br.com.prf.leitordeplacas.service.exception.authentication;

public class JwtAuthenticationException extends RuntimeException {
    public JwtAuthenticationException(String message) {
        super(message);
    }

  public JwtAuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}

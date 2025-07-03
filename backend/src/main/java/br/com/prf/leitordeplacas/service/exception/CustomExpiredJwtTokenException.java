package br.com.prf.leitordeplacas.service.exception;

import br.com.prf.leitordeplacas.service.exception.authentication.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomExpiredJwtTokenException extends JwtAuthenticationException {
    public CustomExpiredJwtTokenException(String message) {
        super(message);
    }

  public CustomExpiredJwtTokenException(String message, Throwable cause) {
    super(message, cause);
  }
}

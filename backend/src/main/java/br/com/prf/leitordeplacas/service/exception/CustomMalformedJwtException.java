package br.com.prf.leitordeplacas.service.exception;

import br.com.prf.leitordeplacas.service.exception.authentication.JwtAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomMalformedJwtException extends JwtAuthenticationException {
    public CustomMalformedJwtException(String message) {
        super(message);
    }

   public CustomMalformedJwtException(String message, Throwable cause) {
     super(message, cause);
   }
}

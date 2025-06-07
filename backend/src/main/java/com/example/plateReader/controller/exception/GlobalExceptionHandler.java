package com.example.plateReader.controller.exception;

import com.example.plateReader.service.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<StandardError> handleAuthenticationException(RuntimeException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Authentication error",
                "Credenciais inválidas",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<StandardError> handleAccessDenied(AuthorizationDeniedException e, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Acess denied",
                "Você não tem permissão para acessar este recurso",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler({ExpiredJwtTokenException.class, JwtAuthenticationException.class, CustomMalformedJwtException.class})
    public ResponseEntity<StandardError> handleJwtException(RuntimeException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Invalid token",
                e instanceof ExpiredJwtTokenException ? "Token expirado" : "Token com assinatura inválida",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<StandardError> handleAuthenticationFailure(AuthenticationException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Authentication failure",
                "As credenciais de autenticação não foram fornecidas ou são inválidas. Acesso não autorizado.",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<StandardError> usernameAlreadyExists(UsernameAlreadyExistsException e, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Username already exists",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AppUserNotFoundException.class)
    public ResponseEntity<StandardError> appUserNotFound(AppUserNotFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "User not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<StandardError> vehicleNotFound(VehicleNotFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Vehicle not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<StandardError> personNotFound(PersonNotFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Person not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CriminalRecordNotFoundException.class)
    public ResponseEntity<StandardError> criminalRecordNotFound(CriminalRecordNotFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Criminal Record not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(CrimeNotFoundException.class)
    public ResponseEntity<StandardError> crimeNotFound(CrimeNotFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Crime not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<StandardError> handleInvalidData(InvalidDataException e, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Resource not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<StandardError> handleNoHandlerFound(NoHandlerFoundException e, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Resource not found",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(OcrProcessingException.class)
    public ResponseEntity<Map<String, String>> handleOcrException(NoHandlerFoundException e) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;

        return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StringBuilder messages = new StringBuilder();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            messages.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        });

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Validation error",
                messages.toString().trim(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }
}

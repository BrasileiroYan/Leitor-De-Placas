package com.example.plateReader.controller.exception;

import com.example.plateReader.service.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<StandardError> handleAuthenticationException(RuntimeException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Authentication error",
                e.getMessage(),
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

    @ExceptionHandler({CustomExpiredJwtTokenException.class, JwtAuthenticationException.class, CustomMalformedJwtException.class})
    public ResponseEntity<StandardError> handleJwtException(RuntimeException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Invalid token",
                e instanceof CustomExpiredJwtTokenException ? "Token expirado" : "Token com assinatura inválida",
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
    public ResponseEntity<Map<String, String>> handleOcrException(OcrProcessingException e) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;

        return ResponseEntity.status(status).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StringBuilder messages = new StringBuilder();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            messages.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }

        for (ObjectError error : e.getBindingResult().getGlobalErrors()) {
            messages.append(error.getDefaultMessage()).append("; ");
        }

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Validation error",
                messages.toString().trim(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidActivationTokenException.class)
    public ResponseEntity<StandardError> handleInvalidActivationTokenException(InvalidActivationTokenException e, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Invalid Activation Token",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(InvalidPasswordResetTokenException.class)
    public ResponseEntity<StandardError> handleInvalidPasswordResetTokenException(InvalidPasswordResetTokenException e, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Invalid Reset Token",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(EmailSendingException.class)
    public ResponseEntity<StandardError> handleEmailSendingException(EmailSendingException e, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Email Service error",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<StandardError> handleSamePasswordException(SamePasswordException e, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Same Password error",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(AccountLockedException.class)
    public ResponseEntity<StandardError> handleAccountLockedException(AccountLockedException e, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Account Locked",
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }

    // DEIXE ESSE HANDLER NO FINAL

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardError> handleAllRuntimeExceptions(RuntimeException e, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        logger.error("Erro inesperado no servidor: {}", e.getMessage(), e);

        StandardError error = new StandardError(
                Instant.now(),
                status.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor. Por favor, tente novamente mais tarde.",
                request.getDescription(false).replace("uri=", "")
        );

        return ResponseEntity.status(status).body(error);
    }
}

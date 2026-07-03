package com.busticket.app.controller;


import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.exceptions.InvalidCredentialsException;
import com.busticket.app.model.dto.ErrorResponse;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public ErrorResponse builderErrorResponse(String message, String errorCode, int statusCode, LocalDateTime timestamp) {
        return ErrorResponse.builder()
                .message(message)
                .errorCode(errorCode)
                .statusCode(statusCode)
                .timestamp(timestamp)
                .build();
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistsException(EntityAlreadyExistsException entityAlreadyExistsException) {
        ErrorResponse error = builderErrorResponse(entityAlreadyExistsException.getMessage(), "ERR_ENTITY_EXISTS",
                HttpStatus.CONFLICT.value(), LocalDateTime.now());
        log.error("EntityAlreadyExistsException {}", entityAlreadyExistsException, entityAlreadyExistsException.getCause());
        return ResponseEntity.status(error.getStatusCode()).body(error);

    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<ErrorResponse> handleStripeException(StripeException stripeException) {
        ErrorResponse error = builderErrorResponse(
                "Greška pri obradi plaćanja", "ERR_PAYMENT", HttpStatus.PAYMENT_REQUIRED.value()
                , LocalDateTime.now());
        log.error("StripeException {}", stripeException.getMessage(), stripeException);
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException){
        ErrorResponse error = builderErrorResponse(entityNotFoundException.getMessage(), "ERR_ENTITY_NOT_FOUND",
                HttpStatus.NOT_FOUND.value(),  LocalDateTime.now());
        log.error("EntityNotFoundException {}", entityNotFoundException, entityNotFoundException.getCause());
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponse error = builderErrorResponse(message, "ERR_VALIDATION",
                HttpStatus.BAD_REQUEST.value(), LocalDateTime.now());
        log.warn("MethodArgumentNotValidException {}", message);
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException invalidCredentialsException){
        ErrorResponse error = builderErrorResponse(invalidCredentialsException.getMessage(), "ERR_INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED.value(), LocalDateTime.now());
        log.error("InvalidCredentialsException {}", invalidCredentialsException, invalidCredentialsException.getCause());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException runtimeException){
        ErrorResponse error = builderErrorResponse("Internal server error", "ERR_INTERNAL",
                HttpStatus.INTERNAL_SERVER_ERROR.value(), 
                LocalDateTime.now());
        log.error("RuntimeException {}", runtimeException, runtimeException.getCause());
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

}
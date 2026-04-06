package com.busticket.app.controller;


import com.busticket.app.exceptions.EntityAlreadyExistsException;
import com.busticket.app.exceptions.EntityNotFoundException;
import com.busticket.app.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
                409, LocalDateTime.now());
        log.error("EntityAlreadyExistsException {}", entityAlreadyExistsException, entityAlreadyExistsException.getCause());
        return ResponseEntity.status(error.getStatusCode()).body(error);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException){
        ErrorResponse error = builderErrorResponse(entityNotFoundException.getMessage(), "ERR_ENTITY_NOT_FOUND",
                404,  LocalDateTime.now());
        log.error("EntityNotFoundException {}", entityNotFoundException, entityNotFoundException.getCause());
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException runtimeException){
        ErrorResponse error = builderErrorResponse("Internal server error", "ERR_INTERNAL", 500,
                LocalDateTime.now());
        log.error("RuntimeException {}", runtimeException, runtimeException.getCause());
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

}
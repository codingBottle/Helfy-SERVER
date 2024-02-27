package com.codingbottle.exception;

import com.codingbottle.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.io.IOException;

@Slf4j
@Primary
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ApplicationErrorException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(WebRequest request, ApplicationErrorException e) {
        log.error("ApplicationErrorException {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getApplicationErrorType().name(), e.getMessage());
        return new ResponseEntity<>(errorResponse, e.getApplicationErrorType().getHttpStatus());
    }

    @ExceptionHandler(value = InvalidDataAccessApiUsageException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDataAccessApiUsageException(WebRequest request, InvalidDataAccessApiUsageException e) {
        log.error("InvalidDataAccessApiUsageException {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ApplicationErrorType.INVALID_DATA_ACCESS.name(), e.getMessage());
        return new ResponseEntity<>(errorResponse, ApplicationErrorType.INTERNAL_ERROR.getHttpStatus());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(WebRequest request, MethodArgumentNotValidException e) {
        log.error("ValidationException {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ApplicationErrorType.VALIDATION_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(errorResponse, ApplicationErrorType.VALIDATION_ERROR.getHttpStatus());
    }

    @ExceptionHandler(value = IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(WebRequest request, IOException e) {
        log.error("IOException {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ApplicationErrorType.INTERNAL_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(errorResponse, ApplicationErrorType.INTERNAL_ERROR.getHttpStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(WebRequest request, ConstraintViolationException e) {
        log.error("ConstraintViolationException {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(ApplicationErrorType.VALIDATION_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(errorResponse, ApplicationErrorType.VALIDATION_ERROR.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(WebRequest request, Exception e) {
        log.error("Exception {}", e.getMessage());
        ErrorResponse response = new ErrorResponse(ApplicationErrorType.INTERNAL_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(response, ApplicationErrorType.INTERNAL_ERROR.getHttpStatus());
    }
}

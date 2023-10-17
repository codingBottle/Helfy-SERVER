package com.codingbottle.common.exception;

import com.codingbottle.common.model.ErrorResponseDto;
import com.google.api.pathtemplate.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@Primary
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = ApplicationErrorException.class)
    public ResponseEntity<ErrorResponseDto> handleApplicationException(WebRequest request, ApplicationErrorException e) {
        log.error("ApplicationErrorException {}", e.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(e.getApplicationErrorType().name(), e.getMessage());
        return new ResponseEntity<>(errorResponseDto, e.getApplicationErrorType().getHttpStatus());
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(WebRequest request, ValidationException e) {
        log.error("ValidationException {}", e.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ApplicationErrorType.VALIDATION_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(errorResponseDto, ApplicationErrorType.VALIDATION_ERROR.getHttpStatus());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(WebRequest request, ConstraintViolationException e) {
        log.error("ConstraintViolationException {}", e.getMessage());
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ApplicationErrorType.VALIDATION_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(errorResponseDto, ApplicationErrorType.VALIDATION_ERROR.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleException(WebRequest request, Exception e) {
        log.error("Exception {}", e.getMessage());
        ErrorResponseDto response = new ErrorResponseDto(ApplicationErrorType.INTERNAL_ERROR.name(), e.getMessage());
        return new ResponseEntity<>(response, ApplicationErrorType.INTERNAL_ERROR.getHttpStatus());
    }
}

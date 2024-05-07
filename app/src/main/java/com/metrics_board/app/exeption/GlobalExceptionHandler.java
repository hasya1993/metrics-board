package com.metrics_board.app.exeption;

import com.metrics_board.app.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        String message = "Internal server error";
        log.error(message, e);
        return ResponseEntity.internalServerError().body(ApiResponse.error(message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException e) {
        String message = e.getFieldErrors().stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Some property is invalid");

        return badRequest(message);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingHeaderException(MissingRequestHeaderException e) {
        return badRequest("Request header 'X-ACCOUNT-ID' is missing");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> MethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        if (e.getParameter().getParameterType() == UUID.class) {
            return badRequest("'X-ACCOUNT-ID' is invalid");
        }
        if (e.getParameter().getParameterType() == Long.class) {
            return badRequest("id is invalid");
        }
        return badRequest("Some property is invalid");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> HttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return badRequest("Required request body is missing");
    }

    @ExceptionHandler(MissedDataToUpdateException.class)
    public ResponseEntity<ApiResponse<Void>> ExceptionResourceNotExit(MissedDataToUpdateException e) {
        return badRequest("Missed data to update");
    }

    @ExceptionHandler(ResourceNotExistException.class)
    public ResponseEntity<ApiResponse<Void>> ExceptionResourceNotExit(ResourceNotExistException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error("Resource not exist"));
    }

    private ResponseEntity<ApiResponse<Void>> badRequest(String message) {
        return ResponseEntity.badRequest().body(ApiResponse.error(message));
    }
}
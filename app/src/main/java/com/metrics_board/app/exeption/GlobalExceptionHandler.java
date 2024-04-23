package com.metrics_board.app.exeption;

import com.metrics_board.app.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        return ResponseEntity.internalServerError().body(ApiResponse.builder()
                .ok(false)
                .errorMessage("Internal server error")
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(ApiResponse.builder()
                .ok(false)
                .errorMessage(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage())
                .build());
    }
}
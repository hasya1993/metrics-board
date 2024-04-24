package com.metrics_board.app.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean ok;
    private T result;
    private String errorMessage;

    public static <T> ApiResponse<T> success(T something) {
        return ApiResponse.<T>builder()
                .ok(true)
                .result(something)
                .build();
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.<T>builder()
                .ok(false)
                .errorMessage(message)
                .build();
    }
}
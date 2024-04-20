package com.metrics_board.persistence.dto.roll;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean ok;
    private T result;
    private String errorMessage;
}
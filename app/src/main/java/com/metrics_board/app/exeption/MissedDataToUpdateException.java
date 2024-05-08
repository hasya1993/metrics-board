package com.metrics_board.app.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissedDataToUpdateException extends RuntimeException {
    private final String message;
}

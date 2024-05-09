package com.metrics_board.persistence.enums.roll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum BoardStatus {
    ACTIVE("active"),
    SUSPENDED("suspended"),
    ARCHIVED("archived");

    private final String value;

    public static Optional<BoardStatus> of(String status) {
        return Stream.of(BoardStatus.values())
                .filter(boardStatus -> boardStatus.getValue().equals(status))
                .findFirst();
    }
}
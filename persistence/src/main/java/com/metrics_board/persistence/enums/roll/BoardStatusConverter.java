package com.metrics_board.persistence.enums.roll;

import jakarta.persistence.AttributeConverter;

import static java.util.Objects.nonNull;

public class BoardStatusConverter implements AttributeConverter<BoardStatus, String> {
    @Override
    public String convertToDatabaseColumn(BoardStatus boardStatus) {
        return nonNull(boardStatus) ? boardStatus.getValue() : null;
    }

    @Override
    public BoardStatus convertToEntityAttribute(String s) {
        if (nonNull(s)) {
            return BoardStatus.of(s)
                    .orElseThrow(() -> new IllegalArgumentException("Not expected Board status value " + s));
        }
        return null;
    }
}

package com.metrics_board.persistence.enums.roll;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum ProjectStatus {
    ACTIVE("active"),
    SUSPENDED("suspended"),
    ARCHIVED("archived");

    private final String value;

    public static Optional<ProjectStatus> of(String status) {
        return Stream.of(ProjectStatus.values())
                .filter(projectStatus -> projectStatus.getValue().equals(status))
                .findFirst();
    }
}
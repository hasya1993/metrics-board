package com.metrics_board.persistence.enums.roll;

import jakarta.persistence.AttributeConverter;

import static java.util.Objects.nonNull;

public class ProjectStatusConverter implements AttributeConverter<ProjectStatus, String> {
    @Override
    public String convertToDatabaseColumn(ProjectStatus projectStatus) {
        return nonNull(projectStatus) ? projectStatus.getValue() : null;
    }

    @Override
    public ProjectStatus convertToEntityAttribute(String s) {
        if (nonNull(s)) {
            return ProjectStatus.of(s)
                    .orElseThrow(() -> new IllegalArgumentException("Not expected Project status value " + s));
        }
        return null;
    }
}

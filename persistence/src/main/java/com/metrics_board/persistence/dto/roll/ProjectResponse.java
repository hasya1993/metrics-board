package com.metrics_board.persistence.dto.roll;

import com.metrics_board.persistence.enums.roll.ProjectStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse {
    private Long id;
    private UUID ownerId;
    private String name;
    private String description;
    private ProjectStatus status;
}

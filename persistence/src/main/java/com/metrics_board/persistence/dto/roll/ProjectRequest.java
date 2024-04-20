package com.metrics_board.persistence.dto.roll;

import com.metrics_board.persistence.enums.roll.ProjectStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    private String name;
    private String description;
    private ProjectStatus status;
}

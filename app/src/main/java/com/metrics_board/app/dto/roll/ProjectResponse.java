package com.metrics_board.app.dto.roll;

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
    private String status;
}

package com.metrics_board.app.dto.roll;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequest {
    @NotEmpty(message = "Missing 'name' property")
    private String name;
    private String description;
    @Pattern(regexp = "^(active|suspended|archived)$", message = "Missing 'status' property")
    private String status;
}

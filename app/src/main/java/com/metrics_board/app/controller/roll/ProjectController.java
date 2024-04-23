package com.metrics_board.app.controller.roll;

import com.metrics_board.app.dto.ApiResponse;
import com.metrics_board.app.dto.roll.ProjectRequest;
import com.metrics_board.app.dto.roll.ProjectResponse;
import com.metrics_board.app.service.roll.ProjectService;
import com.metrics_board.persistence.entity.roll.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/api/v1/project")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @RequestHeader("X-ACCOUNT-ID") UUID ownerId,
            @RequestBody @Validated ProjectRequest request) {
        Project createdProject = projectService.createProject(ownerId, request);
        ProjectResponse projectResponse = projectService.createProjectResponse(createdProject);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ProjectResponse>builder()
                .ok(true)
                .result(projectResponse)
                .build());
    }
}
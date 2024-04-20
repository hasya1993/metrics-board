package com.metrics_board.app.controller;

import com.metrics_board.persistence.dto.roll.ApiResponse;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import com.metrics_board.persistence.dto.roll.ProjectRequest;
import com.metrics_board.persistence.dto.roll.ProjectResponse;
import com.metrics_board.persistence.service.roll.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;


@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/api/v1/project")
    public ResponseEntity<?> createProject(@RequestHeader("X-ACCOUNT-ID") UUID ownerId,
                                           @RequestBody ProjectRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.<ProjectResponse>builder()
                    .ok(false)
                    .errorMessage("Missing 'name' property")
                    .build());
        }

        Project project = Project.builder()
                .ownerId(ownerId)
                .name(request.getName())
                .description(request.getDescription())
                .status(request.getStatus() != null ? request.getStatus() : ProjectStatus.ACTIVE)
                .build();

        try {
            Project createdProject = projectService.createProject(project);
            ProjectResponse projectResponse = ProjectResponse.builder()
                    .id(createdProject.getId())
                    .ownerId(createdProject.getOwnerId())
                    .name(createdProject.getName())
                    .description(createdProject.getDescription())
                    .status(createdProject.getStatus())
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<ProjectResponse>builder()
                    .ok(true)
                    .result(projectResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<ProjectResponse>builder()
                    .ok(false)
                    .errorMessage("Internal server error")
                    .build());
        }
    }
}
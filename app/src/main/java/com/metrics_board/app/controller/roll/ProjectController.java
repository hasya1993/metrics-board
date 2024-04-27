package com.metrics_board.app.controller.roll;

import com.metrics_board.app.dto.ApiResponse;
import com.metrics_board.app.dto.roll.ProjectRequest;
import com.metrics_board.app.dto.roll.ProjectResponse;
import com.metrics_board.app.exeption.ExceptionResourceNotExit;
import com.metrics_board.app.service.roll.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping("/api/v1/project")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @RequestHeader("X-ACCOUNT-ID") UUID ownerId,
            @RequestBody @Validated ProjectRequest request) {
        ProjectResponse projectResponse = projectService.createProject(ownerId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(projectResponse));
    }

    @GetMapping("/api/v1/project/{id}")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(
            @RequestHeader("X-ACCOUNT-ID") UUID ownerId,
            @PathVariable("id") Long id) throws ExceptionResourceNotExit {
        ProjectResponse projectResponse = projectService.getProject(id);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(projectResponse));
    }
}
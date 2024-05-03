package com.metrics_board.app.service.roll;

import com.metrics_board.app.dto.roll.ProjectRequest;
import com.metrics_board.app.dto.roll.ProjectResponse;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectResponse createProject(UUID ownerId, ProjectRequest request) {
        Project project = Project.builder()
                .ownerId(ownerId)
                .name(request.getName())
                .description(request.getDescription())
                .status(ProjectStatus.of(request.getStatus()).orElse(ProjectStatus.ACTIVE))
                .build();

        return createProjectResponse(projectRepository.save(project));
    }

    public ProjectResponse getProject(UUID ownerId, Long id) throws ResourceNotExistException {
        Optional<Project> foundProject = projectRepository.findById(id);
        if (foundProject.isPresent() && foundProject.get().getOwnerId().equals(ownerId)) {
            return createProjectResponse(foundProject.get());
        }
        throw new ResourceNotExistException();
    }

    public List<ProjectResponse> getProjects(UUID ownerId) {
        Optional<List<Project>> foundProjects = projectRepository.findAllByOwnerId(ownerId);
        List<ProjectResponse> projectResponseList = new ArrayList<>();

        if (foundProjects.isPresent()) {
            for (Project project : foundProjects.get()) {
                projectResponseList.add(createProjectResponse(project));
            }
        }

        return projectResponseList;
    }

    private ProjectResponse createProjectResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .ownerId(project.getOwnerId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus().getValue())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
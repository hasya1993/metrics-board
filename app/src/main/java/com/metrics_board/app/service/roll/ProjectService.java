package com.metrics_board.app.service.roll;

import com.metrics_board.app.dto.roll.ProjectRequest;
import com.metrics_board.app.dto.roll.ProjectResponse;
import com.metrics_board.app.exeption.MissedDataToUpdateException;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
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
        List<Project> foundProjects = projectRepository.findAllByOwnerId(ownerId);
        List<ProjectResponse> projectResponseList = new ArrayList<>();

        for (Project project : foundProjects) {
            projectResponseList.add(createProjectResponse(project));
        }

        return projectResponseList;
    }

    public ProjectResponse updateProject(UUID ownerId, Long id, ProjectRequest request) throws ResourceNotExistException {
        Optional<Project> foundProject = projectRepository.findById(id);

        if (foundProject.isEmpty() || !foundProject.get().getOwnerId().equals(ownerId)) {
            throw new ResourceNotExistException();
        }

        validateProjectRequest(request);

        Project updateProject = foundProject.get();

        updateProjectFields(request, updateProject);

        return createProjectResponse(projectRepository.save(updateProject));
    }

    public void deleteProject(UUID ownerId, Long id) {
        Optional<Project> foundProject = projectRepository.findById(id);
        if (foundProject.isPresent()) {
            if (foundProject.get().getOwnerId().equals(ownerId)) {
                projectRepository.deleteById(id);
            } else {
                log.warn("{} tried to delete project id {} that is not owned by", ownerId, id);
            }
        } else {
            log.warn("{} tried to delete project id {} that does not exist", ownerId, id);
        }
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

    private void validateProjectRequest(ProjectRequest request) {
        if (ObjectUtils.allNull(request.getName(), request.getDescription(), request.getStatus())) {
            throw new MissedDataToUpdateException("Missed data to update");
        }

        if (request.getName() != null && request.getName().equals("")) {
            throw new MissedDataToUpdateException("Invalid 'name' to update");
        }

        if (request.getStatus() != null && !request.getStatus().matches("^(active|suspended|archived)$")) {
            throw new MissedDataToUpdateException("Invalid 'status' to update");
        }
    }

    private void updateProjectFields(ProjectRequest request, Project updateProject) {
        if (request.getName() != null) {
            updateProject.setName(request.getName());
        }

        if (request.getDescription() != null) {
            updateProject.setDescription(request.getDescription().equals("") ? null : request.getDescription());
        }

        ProjectStatus.of(request.getStatus()).ifPresent(updateProject::setStatus);
    }
}
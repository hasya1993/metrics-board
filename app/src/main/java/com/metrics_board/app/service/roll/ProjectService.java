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

    public ProjectResponse updateProject(UUID ownerId, Long id, ProjectRequest request) throws ResourceNotExistException, MissedDataToUpdateException {
        Optional<Project> foundProject = projectRepository.findById(id);
        if (foundProject.isPresent() && foundProject.get().getOwnerId().equals(ownerId)) {
            if (request.getName() == null && request.getDescription() == null && request.getStatus() == null) {
                throw new MissedDataToUpdateException();
            }

            Project updateProject = foundProject.get();

            if (request.getName() != null) {
                if (!request.getName().equals("")) {
                    updateProject.setName(request.getName());
                } else {
                    throw new MissedDataToUpdateException();
                }
            }

            if (request.getDescription() != null) {
                if (!request.getDescription().equals("")) {
                    updateProject.setDescription(request.getDescription());
                } else {
                    updateProject.setDescription(null);
                }
            }

            if (request.getStatus() != null) {
                if (request.getStatus().matches("^(active|suspended|archived)$")) {
                    updateProject.setStatus(ProjectStatus.of(request.getStatus()).get());
                } else {
                    throw new MissedDataToUpdateException();
                }
            }

            return createProjectResponse(projectRepository.save(updateProject));
        }
        throw new ResourceNotExistException();
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
}
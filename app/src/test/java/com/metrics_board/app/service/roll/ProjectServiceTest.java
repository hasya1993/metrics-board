package com.metrics_board.app.service.roll;

import com.metrics_board.app.dto.roll.ProjectRequest;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService;

    @Test
    public void testCreateProject() {
        UUID ownerId = UUID.randomUUID();
        ProjectRequest request = ProjectRequest.builder()
                .name("Test")
                .build();

        Project project = Project.builder()
                .ownerId(ownerId)
                .name(request.getName())
                .description(null)
                .status(ProjectStatus.ACTIVE)
                .build();

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        projectService.createProject(ownerId, request);

        verify(projectRepository).save(any(Project.class));
    }
}
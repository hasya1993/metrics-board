package com.metrics_board.app.service.roll;

import com.metrics_board.app.dto.roll.ProjectRequest;
import com.metrics_board.app.dto.roll.ProjectResponse;
import com.metrics_board.app.exeption.MissedDataToUpdateException;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    private static final Long ID = 1000L;
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final String NAME = "Test";
    private static final ProjectStatus STATUS = ProjectStatus.ACTIVE;
    private static final LocalDateTime UPDATED_AT = LocalDateTime.now();

    @Mock
    ProjectRepository projectRepository;

    @InjectMocks
    ProjectService projectService;

    @Test
    public void testCreateProject() {
        ProjectRequest request = ProjectRequest.builder()
                .name(NAME)
                .build();

        Project project = Project.builder()
                .ownerId(OWNER_ID)
                .name(request.getName())
                .description(null)
                .status(ProjectStatus.ACTIVE)
                .build();

        when(projectRepository.save(any(Project.class))).thenReturn(project);

        projectService.createProject(OWNER_ID, request);

        verify(projectRepository).save(any(Project.class));
    }

    @Test
    public void testGetProject() throws ResourceNotExistException {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .name(NAME)
                .status(ProjectStatus.ACTIVE)
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        ProjectResponse response = projectService.getProject(OWNER_ID, ID);

        verify(projectRepository).findById(ID);
        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(OWNER_ID, response.getOwnerId());
    }

    @Test
    public void testGetProjectNotFound() {
        when(projectRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotExistException.class, () -> projectService.getProject(OWNER_ID, ID));
    }

    @Test
    public void testGetProjectWrongOwner() {
        Project project = Project.builder().id(ID).ownerId(UUID.randomUUID()).build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        assertThrows(ResourceNotExistException.class, () -> projectService.getProject(OWNER_ID, ID));
    }

    @Test
    public void testGetProjects() {
        List<Project> projects = List.of(
                Project.builder().id(ID).ownerId(OWNER_ID).name(NAME).status(STATUS).build(),
                Project.builder().id(ID + 1).ownerId(OWNER_ID).name(NAME).status(STATUS).build()
        );

        when(projectRepository.findAllByOwnerId(OWNER_ID)).thenReturn(projects);

        List<ProjectResponse> projectResponseList = projectService.getProjects(OWNER_ID);

        verify(projectRepository).findAllByOwnerId(OWNER_ID);
        assertEquals(2, projectResponseList.size());
        assertEquals(ID, projectResponseList.get(0).getId());
        assertEquals(OWNER_ID, projectResponseList.get(0).getOwnerId());
        assertEquals(ID + 1, projectResponseList.get(1).getId());
        assertEquals(OWNER_ID, projectResponseList.get(1).getOwnerId());
    }

    @Test
    public void testGetProjectsEmptyList() {
        when(projectRepository.findAllByOwnerId(OWNER_ID)).thenReturn(new ArrayList<>());

        List<ProjectResponse> projectResponseList = projectService.getProjects(OWNER_ID);

        verify(projectRepository).findAllByOwnerId(OWNER_ID);
        assertEquals(0, projectResponseList.size());
    }

    @Test
    public void testUpdateProject() throws ResourceNotExistException, MissedDataToUpdateException {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .status(ProjectStatus.ACTIVE)
                .build();

        Project updateProject = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .status(ProjectStatus.SUSPENDED)
                .updatedAt(UPDATED_AT)
                .build();

        ProjectRequest request = ProjectRequest.builder()
                .status(ProjectStatus.SUSPENDED.getValue())
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updateProject);

        ProjectResponse response = projectService.updateProject(OWNER_ID, ID, request);

        verify(projectRepository).findById(ID);
        verify(projectRepository).save(any(Project.class));
        assertNotNull(response);
        assertEquals(ID, response.getId());
        assertEquals(OWNER_ID, response.getOwnerId());
        assertEquals(UPDATED_AT, response.getUpdatedAt());
    }

    @Test
    public void testUpdateProjectNotFound() {
        ProjectRequest request = ProjectRequest.builder()
                .status(ProjectStatus.SUSPENDED.getValue())
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotExistException.class, () -> projectService.updateProject(OWNER_ID, ID, request));
    }

    @Test
    public void testUpdateProjectWrongOwner() {
        Project project = Project.builder().id(ID).ownerId(UUID.randomUUID()).build();

        ProjectRequest request = ProjectRequest.builder()
                .status(ProjectStatus.SUSPENDED.getValue())
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        assertThrows(ResourceNotExistException.class, () -> projectService.updateProject(OWNER_ID, ID, request));
    }

    @Test
    public void testUpdateProjectBodyIsEmpty() {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .status(ProjectStatus.ACTIVE)
                .build();

        ProjectRequest request = ProjectRequest.builder().build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        assertThrows(MissedDataToUpdateException.class, () -> projectService.updateProject(OWNER_ID, ID, request));
    }

    @Test
    public void testUpdateProjectNameIsInvalid() {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .status(ProjectStatus.ACTIVE)
                .build();

        ProjectRequest request = ProjectRequest.builder().name("").build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        assertThrows(MissedDataToUpdateException.class, () -> projectService.updateProject(OWNER_ID, ID, request));
    }

    @Test
    public void testUpdateProjectStatusIsInvalid() {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .status(ProjectStatus.ACTIVE)
                .build();

        ProjectRequest request = ProjectRequest.builder().status("").build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        assertThrows(MissedDataToUpdateException.class, () -> projectService.updateProject(OWNER_ID, ID, request));
    }

    @Test
    public void testDeleteProject() {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        projectService.deleteProject(OWNER_ID, ID);

        verify(projectRepository).findById(ID);
        verify(projectRepository).deleteById(ID);
    }

    @Test
    public void testDeleteProjectNotOwner() {
        Project project = Project.builder()
                .id(ID)
                .ownerId(OWNER_ID)
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        projectService.deleteProject(UUID.randomUUID(), ID);

        verify(projectRepository, never()).deleteById(ID);
    }

    @Test
    public void testDeleteProjectNotExist() {
        when(projectRepository.findById(ID)).thenReturn(Optional.empty());

        projectService.deleteProject(OWNER_ID, ID);

        verify(projectRepository, never()).deleteById(ID);
    }
}
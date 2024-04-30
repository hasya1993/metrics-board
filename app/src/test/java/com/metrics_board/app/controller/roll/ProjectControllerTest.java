package com.metrics_board.app.controller.roll;

import com.metrics_board.app.dto.roll.ProjectResponse;
import com.metrics_board.app.exeption.ResourceNotExitException;
import com.metrics_board.app.service.roll.ProjectService;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProjectController.class)
public class ProjectControllerTest {
    private static final Long ID = 1000L;
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final String NAME = "Test";
    private static final ProjectStatus STATUS = ProjectStatus.ACTIVE;
    public static final String INVALID_VALUE = "invalid value";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Test
    public void testCreateProject() throws Exception {
        when(projectService.createProject(eq(OWNER_ID), any()))
                .thenReturn(ProjectResponse.builder().ownerId(OWNER_ID).name(NAME).status(STATUS.getValue()).build());

        this.mockMvc.perform(post("/api/v1/project")
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {       
                                "name" : "Test"  
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(true),
                        jsonPath("$.result.ownerId").value(OWNER_ID.toString()),
                        jsonPath("$.result.name").value(NAME),
                        jsonPath("$.result.status").value(STATUS.getValue()),
                        jsonPath("$.errorMessage").doesNotHaveJsonPath()
                );
    }

    @Test
    public void testMissingNamePropertyWithEmptyName() throws Exception {
        when(projectService.createProject(eq(OWNER_ID), any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project")
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {       
                                "name" : ""  
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Missing 'name' property")
                );
    }

    @Test
    public void testMissingNamePropertyWithNullName() throws Exception {
        when(projectService.createProject(eq(OWNER_ID), any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project")
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Missing 'name' property")
                );
    }

    @Test
    public void testUnexpectedStatusValue() throws Exception {
        when(projectService.createProject(eq(OWNER_ID), any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project")
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {       
                                "name" : "Test",  
                                "status" : "action"  
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Unexpected 'status' value")
                );
    }

    @Test
    public void testRequestHeaderX_ACCOUNT_IDIsMissingWhenCreateProject() throws Exception {
        when(projectService.createProject(any(), any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {       
                                "name" : "Test"  
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Request header 'X-ACCOUNT-ID' is missing")
                );
    }

    @Test
    public void testX_ACCOUNT_IDInvalidWhenCreateProject() throws Exception {
        when(projectService.createProject(any(), any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project")
                        .header("X-ACCOUNT-ID", INVALID_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)

                        .content("""
                                {       
                                "name" : "Test"  
                                }
                                """))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("'X-ACCOUNT-ID' is invalid")
                );
    }

    @Test
    public void testRequiredRequestBodyIsMissing() throws Exception {
        when(projectService.createProject(eq(OWNER_ID), any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project")
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Required request body is missing")
                );
    }

    @Test
    public void testGetProject() throws Exception {
        when(projectService.getProject(eq(OWNER_ID),eq(ID)))
                .thenReturn(ProjectResponse.builder().id(ID).ownerId(OWNER_ID).name(NAME).status(STATUS.getValue()).build());

        this.mockMvc.perform(get("/api/v1/project/{id}", ID)
                        .header("X-ACCOUNT-ID", OWNER_ID))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(true),
                        jsonPath("$.result.id").value(ID),
                        jsonPath("$.result.ownerId").value(OWNER_ID.toString()),
                        jsonPath("$.result.name").value(NAME),
                        jsonPath("$.result.status").value(STATUS.getValue()),
                        jsonPath("$.errorMessage").doesNotHaveJsonPath()
                );
    }

    @Test
    public void testResourceNotExist() throws Exception {
        when(projectService.getProject(any(),any()))
                .thenThrow(new ResourceNotExitException());

        this.mockMvc.perform(get("/api/v1/project/{id}", ID)
                        .header("X-ACCOUNT-ID", OWNER_ID))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Resource not exist")
                );
    }

    @Test
    public void testRequestHeaderX_ACCOUNT_IDIsMissingWhenGetProject() throws Exception {
        when(projectService.getProject(any(),any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(get("/api/v1/project/{id}", ID))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Request header 'X-ACCOUNT-ID' is missing")
                );
    }

    @Test
    public void testX_ACCOUNT_IDInvalidWhenGetProject() throws Exception {
        when(projectService.getProject(any(),any()))
                .thenReturn(ProjectResponse.builder().build());

        this.mockMvc.perform(get("/api/v1/project/{id}", ID)
                        .header("X-ACCOUNT-ID", INVALID_VALUE))
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("'X-ACCOUNT-ID' is invalid")
                );
    }
}
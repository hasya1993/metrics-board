package com.metrics_board.app.controller.roll;

import com.metrics_board.app.dto.roll.BoardResponse;
import com.metrics_board.app.exeption.NoRightsException;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.app.service.roll.BoardService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BoardController.class)
public class BoardControllerTest {
    private static final Long ID = 1000L;
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final String NAME = "Test";
    private static final ProjectStatus STATUS = ProjectStatus.ACTIVE;
    private static final String INVALID_VALUE = "invalid value";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardService boardService;

    @Test
    public void testCreateBoard() throws Exception {
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().projectId(ID).name(NAME).status(STATUS.getValue()).build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Test"
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(true),
                        jsonPath("$.result.projectId").value(ID),
                        jsonPath("$.result.name").value(NAME),
                        jsonPath("$.result.status").value(STATUS.getValue()),
                        jsonPath("$.errorMessage").doesNotHaveJsonPath()
                );
    }

    @Test
    public void testMissingNamePropertyWithEmptyName() throws Exception {
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
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
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
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
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name" : "Test",
                                "status" : "DataIsInvalid"
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
    public void testRequestHeaderX_ACCOUNT_IDIsMissingWhenCreateBoard() throws Exception {
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
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
    public void testX_ACCOUNT_IDInvalidWhenCreateBoard() throws Exception {
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
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
    public void testRequiredRequestBodyIsMissingWhenCreateBoard() throws Exception {
        when(boardService.createBoard(eq(OWNER_ID), any(), eq(ID)))
                .thenReturn(BoardResponse.builder().build());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
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
    public void testResourceNotExistWhenCreateBoard() throws Exception {
        when(boardService.createBoard(any(), any(), any()))
                .thenThrow(new ResourceNotExistException());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Test"
                                }
                                """))
                .andExpectAll(
                        status().isNotFound(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("Resource not exist")
                );
    }

    @Test
    public void testNoRightsWhenCreateBoard() throws Exception {
        when(boardService.createBoard(any(), any(), any()))
                .thenThrow(new NoRightsException());

        this.mockMvc.perform(post("/api/v1/project/{id}/board", ID)
                        .header("X-ACCOUNT-ID", OWNER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "name": "Test"
                                }
                                """))
                .andExpectAll(
                        status().isForbidden(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.ok").value(false),
                        jsonPath("$.errorMessage").value("No rights")
                );
    }
}
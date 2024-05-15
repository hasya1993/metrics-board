package com.metrics_board.app.service.roll;

import com.metrics_board.app.dto.roll.BoardRequest;
import com.metrics_board.app.exeption.NoRightsException;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.persistence.entity.roll.Board;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.BoardStatus;
import com.metrics_board.persistence.repository.roll.BoardRepository;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    private static final Long ID = 1000L;
    private static final UUID OWNER_ID = UUID.randomUUID();
    private static final String NAME = "Test";
    private static final BoardStatus STATUS = BoardStatus.ACTIVE;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    @Test
    public void testCreateBoard() throws NoRightsException, ResourceNotExistException {
        BoardRequest request = BoardRequest.builder()
                .name(NAME)
                .build();

        Project project = Project.builder()
                .ownerId(OWNER_ID)
                .build();

        Board board = Board.builder()
                .projectId(ID)
                .name(request.getName())
                .description(null)
                .status(STATUS)
                .build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        when(boardRepository.save(any(Board.class))).thenReturn(board);

        boardService.createBoard(OWNER_ID, request, ID);

        verify(projectRepository).findById(ID);
        verify(boardRepository).save(any(Board.class));
    }

    @Test
    public void testCreateBoardNotFound() {
        BoardRequest request = BoardRequest.builder().build();

        when(projectRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(ResourceNotExistException.class, () -> boardService.createBoard(OWNER_ID, request, ID));
    }

    @Test
    public void testCreateBoardNoRights() {
        BoardRequest request = BoardRequest.builder().build();

        Project project = Project.builder().ownerId(UUID.randomUUID()).build();

        when(projectRepository.findById(ID)).thenReturn(Optional.of(project));

        assertThrows(NoRightsException.class, () -> boardService.createBoard(OWNER_ID, request, ID));
    }
}
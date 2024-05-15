package com.metrics_board.app.service.roll;

import com.metrics_board.app.dto.roll.BoardRequest;
import com.metrics_board.app.dto.roll.BoardResponse;
import com.metrics_board.app.exeption.NoRightsException;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.persistence.entity.roll.Board;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.enums.roll.BoardStatus;
import com.metrics_board.persistence.repository.roll.BoardRepository;
import com.metrics_board.persistence.repository.roll.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;

    public BoardResponse createBoard(UUID ownerId, BoardRequest request, Long id) throws NoRightsException, ResourceNotExistException {
        Optional<Project> foundProject = projectRepository.findById(id);

        if (foundProject.isEmpty()) {
            throw new ResourceNotExistException();
        }

        if (foundProject.get().getOwnerId().equals(ownerId)) {
            Board board = Board.builder()
                    .projectId(id)
                    .name(request.getName())
                    .description(request.getDescription())
                    .status(BoardStatus.of(request.getStatus()).orElse(BoardStatus.ACTIVE))
                    .build();

            return createBoardResponse(boardRepository.save(board));
        }

        throw new NoRightsException();
    }

    private BoardResponse createBoardResponse(Board board) {
        return BoardResponse.builder()
                .id(board.getId())
                .name(board.getName())
                .description(board.getDescription())
                .status(board.getStatus().getValue())
                .createdAt(board.getCreatedAt())
                .updatedAt(board.getUpdatedAt())
                .build();
    }
}
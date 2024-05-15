package com.metrics_board.app.controller.roll;

import com.metrics_board.app.dto.ApiResponse;
import com.metrics_board.app.dto.roll.BoardRequest;
import com.metrics_board.app.dto.roll.BoardResponse;
import com.metrics_board.app.exeption.NoRightsException;
import com.metrics_board.app.exeption.ResourceNotExistException;
import com.metrics_board.app.service.roll.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/api/v1/project/{id}/board")
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(
            @RequestHeader("X-ACCOUNT-ID") UUID ownerId,
            @RequestBody @Validated BoardRequest request,
            @PathVariable("id") Long id) throws NoRightsException, ResourceNotExistException {
        BoardResponse boardResponse = boardService.createBoard(ownerId, request, id);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(boardResponse));
    }
}
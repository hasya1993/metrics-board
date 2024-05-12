package com.metrics_board.persistence.entity.roll;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(schema = "roll", name = "project_boards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ProjectBoards {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "project_boards_id_seq")
    @SequenceGenerator(name = "project_boards_id_seq", sequenceName = "roll.project_boards_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
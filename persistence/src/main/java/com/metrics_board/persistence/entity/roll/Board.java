package com.metrics_board.persistence.entity.roll;

import com.metrics_board.persistence.enums.roll.ProjectStatus;
import com.metrics_board.persistence.enums.roll.ProjectStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(schema = "roll", name = "board")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_id_seq")
    @SequenceGenerator(name = "board_id_seq", sequenceName = "roll.board_id_seq", allocationSize = 1, initialValue = 1000)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "name", nullable = false, length = 1024)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status", nullable = false)
    @Convert(converter = ProjectStatusConverter.class)
    private ProjectStatus status;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
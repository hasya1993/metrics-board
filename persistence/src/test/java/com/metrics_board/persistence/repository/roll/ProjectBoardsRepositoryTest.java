package com.metrics_board.persistence.repository.roll;

import com.metrics_board.persistence.PostgresTestContainer;
import com.metrics_board.persistence.TestApplication;
import com.metrics_board.persistence.entity.roll.Board;
import com.metrics_board.persistence.entity.roll.Project;
import com.metrics_board.persistence.entity.roll.ProjectBoards;
import com.metrics_board.persistence.enums.roll.ProjectStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

@DataJpaTest
@ExtendWith(PostgresTestContainer.class)
@SpringJUnitConfig(TestApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectBoardsRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ProjectBoardsRepository projectBoardsRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testRetrieve() {
        Assertions.assertThat(projectBoardsRepository.findById(1L))
                .isPresent()
                .get()
                .satisfies(
                        foundProjectBoards -> Assertions.assertThat(foundProjectBoards.getProjectId()).isEqualTo(1000),
                        foundProjectBoards -> Assertions.assertThat(foundProjectBoards.getBoardId()).isEqualTo(1000),
                        foundProjectBoards -> Assertions.assertThat(foundProjectBoards.getCreatedAt()).isNotNull());
    }

    @Test
    public void testInsertAndRetrieve() {
        Project project = new Project();
        project.setOwnerId(UUID.randomUUID());
        project.setName("Project");
        project.setStatus(ProjectStatus.ACTIVE);
        projectRepository.save(project);

        Board board = new Board();
        board.setProjectId(project.getId());
        board.setName("Board");
        board.setStatus(ProjectStatus.ACTIVE);
        boardRepository.save(board);

        ProjectBoards projectBoards = new ProjectBoards();
        projectBoards.setProjectId(project.getId());
        projectBoards.setBoardId(board.getId());
        projectBoardsRepository.save(projectBoards);

        em.flush();
        em.clear();

        Assertions.assertThat(projectBoardsRepository.findById(projectBoards.getId()))
                .isPresent()
                .get()
                .satisfies(
                        foundProjectBoards -> Assertions.assertThat(foundProjectBoards.getProjectId()).isEqualTo(project.getId()),
                        foundProjectBoards -> Assertions.assertThat(foundProjectBoards.getBoardId()).isEqualTo(board.getId()),
                        foundProjectBoards -> Assertions.assertThat(foundProjectBoards.getCreatedAt()).isNotNull());
    }
}
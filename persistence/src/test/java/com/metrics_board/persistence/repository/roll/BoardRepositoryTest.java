package com.metrics_board.persistence.repository.roll;

import com.metrics_board.persistence.PostgresTestContainer;
import com.metrics_board.persistence.TestApplication;
import com.metrics_board.persistence.entity.roll.Board;
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

@DataJpaTest
@ExtendWith(PostgresTestContainer.class)
@SpringJUnitConfig(TestApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardRepositoryTest {
    @Autowired
    private BoardRepository boardRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testRetrieve() {
        Assertions.assertThat(boardRepository.findById(1000L))
                .isPresent()
                .get()
                .satisfies(
                        foundBoard -> Assertions.assertThat(foundBoard.getProjectId()).isEqualTo(1000),
                        foundBoard -> Assertions.assertThat(foundBoard.getName()).isEqualTo("Board"),
                        foundBoard -> Assertions.assertThat(foundBoard.getDescription()).isEqualTo("description"),
                        foundBoard -> Assertions.assertThat(foundBoard.getStatus()).isEqualTo(ProjectStatus.ACTIVE),
                        foundBoard -> Assertions.assertThat(foundBoard.getCreatedAt()).isNotNull());
    }

    @Test
    public void testInsertAndRetrieve() {
        Board board = new Board();
        board.setProjectId(1000L);
        board.setName("Test");
        board.setDescription("description");
        board.setStatus(ProjectStatus.ACTIVE);
        boardRepository.save(board);

        em.flush();
        em.clear();

        Assertions.assertThat(boardRepository.findById(board.getId()))
                .isPresent()
                .get()
                .satisfies(
                        foundBoard -> Assertions.assertThat(foundBoard.getProjectId()).isEqualTo(1000),
                        foundBoard -> Assertions.assertThat(foundBoard.getName()).isEqualTo("Test"),
                        foundBoard -> Assertions.assertThat(foundBoard.getDescription()).isEqualTo("description"),
                        foundBoard -> Assertions.assertThat(foundBoard.getStatus()).isEqualTo(ProjectStatus.ACTIVE),
                        foundBoard -> Assertions.assertThat(foundBoard.getCreatedAt()).isNotNull());
    }
}
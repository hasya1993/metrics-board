package com.metrics_board.persistence.repository;

import com.metrics_board.persistence.PostgresTestContainer;
import com.metrics_board.persistence.TestApplication;
import com.metrics_board.persistence.entity.Project;
import com.metrics_board.persistence.enums.ProjectStatus;
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
public class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testRetrieve() {
        Assertions.assertThat(projectRepository.findById(1000L))
                .isPresent()
                .get()
                .satisfies(
                        foundProject -> Assertions.assertThat(foundProject.getOwnerId()).isNotNull(),
                        foundProject -> Assertions.assertThat(foundProject.getName()).isEqualTo("Project"),
                        foundProject -> Assertions.assertThat(foundProject.getDescription()).isEqualTo("description"),
                        foundProject -> Assertions.assertThat(foundProject.getStatus()).isEqualTo(ProjectStatus.active),
                        foundProject -> Assertions.assertThat(foundProject.getCreatedAt()).isNotNull());
    }

    @Test
    public void testInsertAndRetrieve() {
        Project project = new Project();
        project.setOwnerId(UUID.randomUUID());
        project.setName("Test");
        project.setDescription("description");
        project.setStatus(ProjectStatus.active);
        projectRepository.save(project);

        em.flush();
        em.clear();

        Assertions.assertThat(projectRepository.findById(project.getId()))
                .isPresent()
                .get()
                .satisfies(
                        foundProject -> Assertions.assertThat(foundProject.getOwnerId()).isEqualTo(project.getOwnerId()),
                        foundProject -> Assertions.assertThat(foundProject.getName()).isEqualTo("Test"),
                        foundProject -> Assertions.assertThat(foundProject.getDescription()).isEqualTo("description"),
                        foundProject -> Assertions.assertThat(foundProject.getStatus()).isEqualTo(ProjectStatus.active),
                        foundProject -> Assertions.assertThat(foundProject.getCreatedAt()).isNotNull());
    }
}
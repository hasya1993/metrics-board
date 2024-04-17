package com.metrics_board.persistence.repository;

import com.metrics_board.persistence.PostgresTestContainer;
import com.metrics_board.persistence.TestApplication;
import com.metrics_board.persistence.entity.Demo;
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
public class DemoRepositoryTest {
    @Autowired
    private DemoRepository demoRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testEntityWithIdOneExists() {
        Assertions.assertThat(demoRepository.findById(1L))
                .isPresent()
                .get()
                .satisfies(
                        demo -> Assertions.assertThat(demo.getName()).isEqualTo("Sasha"),
                        demo -> Assertions.assertThat(demo.getCreatedAt()).isNotNull());
    }

    @Test
    public void testCreateAndGet() {
        Demo demo = new Demo();
        demo.setName("Test");
        demoRepository.save(demo);

        em.flush();
        em.clear();

        Assertions.assertThat(demoRepository.findById(demo.getId()))
                .isPresent()
                .get()
                .satisfies(
                        createDemo -> Assertions.assertThat(createDemo.getName()).isEqualTo("Test"),
                        createDemo -> Assertions.assertThat(createDemo.getCreatedAt()).isNotNull());
    }
}
package com.metrics_board.persistence;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class PostgresTestContainer implements BeforeAllCallback, AfterAllCallback {
    private PostgreSQLContainer<?> postgres;

    @Override
    public void beforeAll(ExtensionContext context) {
        postgres = new PostgreSQLContainer<>("postgres:16.2-alpine3.19")
                .withDatabaseName("testdb")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432);

        postgres.start();
        System.setProperty("spring.flyway.url",
                String.format("jdbc:postgresql://%s:%d/%s",
                        postgres.getHost(), postgres.getFirstMappedPort(), postgres.getDatabaseName()));
        System.setProperty("spring.flyway.user", postgres.getUsername());
        System.setProperty("spring.flyway.password", postgres.getPassword());

        System.setProperty("spring.datasource.url",
                String.format("jdbc:postgresql://%s:%d/%s",
                        postgres.getHost(), postgres.getFirstMappedPort(), postgres.getDatabaseName()));
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // do nothing, Testcontainers handles container shutdown
    }
}
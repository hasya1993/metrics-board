package com.metrics_board.app.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.metrics_board.persistence")
@EntityScan(basePackages = "com.metrics_board.persistence")
public class AnnotationConfig {
}
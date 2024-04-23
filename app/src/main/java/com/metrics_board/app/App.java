package com.metrics_board.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.metrics_board.persistence")
@EntityScan(basePackages = "com.metrics_board.persistence")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
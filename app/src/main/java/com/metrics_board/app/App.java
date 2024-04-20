package com.metrics_board.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.metrics_board")
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
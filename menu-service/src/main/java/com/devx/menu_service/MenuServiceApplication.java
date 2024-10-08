package com.devx.menu_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class MenuServiceApplication {

    private final Integer threadPoolSize;
    private final Integer taskQueueSize;

    public static void main(String[] args) {
        SpringApplication.run(MenuServiceApplication.class, args);
    }

    @Autowired
    public MenuServiceApplication(@Value("${app.threadPoolSize:5}") Integer threadPoolSize, @Value("${app.taskQueueSize:50}") Integer taskQueueSize) {
        this.threadPoolSize = threadPoolSize;
        this.taskQueueSize = taskQueueSize;
    }

    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
    }

}

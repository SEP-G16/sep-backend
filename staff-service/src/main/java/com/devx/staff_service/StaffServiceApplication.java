package com.devx.staff_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class StaffServiceApplication {

	private final Integer threadPoolSize;
	private final Integer taskQueueSize;

	public static void main(String[] args) {
		SpringApplication.run(StaffServiceApplication.class, args);
	}

	@Autowired
	public StaffServiceApplication(
			@Value("${app.threadPoolSize:5}") Integer threadPoolSize,
			@Value("${app.taskQueueSize:50}") Integer taskQueueSize
	) {
		this.threadPoolSize = threadPoolSize;
		this.taskQueueSize = taskQueueSize;
	}

	@Bean
	public Scheduler jdbcScheduler(){
		return Schedulers.newBoundedElastic(threadPoolSize, taskQueueSize, "jdbc-pool");
	}
}

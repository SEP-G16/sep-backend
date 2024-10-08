package com.devx.review_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@SpringBootApplication
public class ReviewServiceApplication {

	public static final Logger LOG = LoggerFactory.getLogger(ReviewServiceApplication.class);

	private final Integer threadPoolSize;

	private final Integer taskQueueSize;

	public static void main(String[] args) {
		SpringApplication.run(ReviewServiceApplication.class, args);
	}

	@Autowired
	public ReviewServiceApplication(
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

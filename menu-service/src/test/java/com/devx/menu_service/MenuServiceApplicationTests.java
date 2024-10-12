package com.devx.menu_service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration
@ActiveProfiles(value = "test")
@Testcontainers
class MenuServiceApplicationTests {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

	@DynamicPropertySource
	static void configureTestProperties(DynamicPropertyRegistry registry){
		registry.add("spring.datasource.url",() -> mySQLContainer.getJdbcUrl());
		registry.add("spring.datasource.username",() -> mySQLContainer.getUsername());
		registry.add("spring.datasource.password",() -> mySQLContainer.getPassword());
		registry.add("spring.jpa.hibernate.ddl-auto",() -> "create");
	}

	@Test
	void contextLoads() {
	}


}

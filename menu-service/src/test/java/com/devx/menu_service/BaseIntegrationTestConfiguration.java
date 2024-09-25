package com.devx.menu_service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Testcontainers
@ContextConfiguration
@ActiveProfiles(value = "test")
public class BaseIntegrationTestConfiguration {
    public static final Logger LOG = LoggerFactory.getLogger(BaseIntegrationTestConfiguration.class);

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
    void isContainerRunningTest()
    {
        Assertions.assertTrue(mySQLContainer.isRunning());
    }
}

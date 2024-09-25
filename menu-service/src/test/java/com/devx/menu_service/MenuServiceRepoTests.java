package com.devx.menu_service;

import com.devx.menu_service.repository.MenuItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public class MenuServiceRepoTests {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

    @Test
    void test()
    {
        Assertions.assertThat(mySQLContainer.isRunning()).isTrue();
    }

    @Test
    void insertionTest()
    {

    }
}

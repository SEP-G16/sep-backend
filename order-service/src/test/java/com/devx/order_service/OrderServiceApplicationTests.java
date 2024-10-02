package com.devx.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
@ActiveProfiles(value = "test")
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}

package com.devx.menu_service;

import com.devx.menu_service.model.Category;
import com.devx.menu_service.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class CategoryServiceTests {

    @MockBean
    CategoryRepository categoryRepository;

    @Test
    void nonEmptyCheck() {
        List<Category> mockCategories = List.of(
                new Category(1L, "Appetizers"),
                new Category(2L, "Breakfast")
        );

        when(categoryRepository.findAll()).thenReturn(mockCategories);

        List<Category> categories = categoryRepository.findAll();

        assertFalse(categories.isEmpty());
    }
}

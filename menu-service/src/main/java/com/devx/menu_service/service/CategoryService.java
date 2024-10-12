package com.devx.menu_service.service;

import com.devx.menu_service.dto.CategoryDto;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.service.integration.CategoryServiceIntegration;
import com.devx.menu_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoryService {


    private final CategoryServiceIntegration categoryServiceIntegration;

    @Autowired
    public CategoryService(CategoryServiceIntegration categoryServiceIntegration) {
        this.categoryServiceIntegration = categoryServiceIntegration;
    }

    public Mono<CategoryDto> addCategory(CategoryDto categoryDto) {
        Category category = AppUtils.CategoryUtils.dtoToEntity(categoryDto);
        return categoryServiceIntegration.addCategory(category);
    }

    public Flux<CategoryDto> getAllCategories() {
        return categoryServiceIntegration.getAllCategories();
    }
}

package com.devx.menu_service.service.integration;

import com.devx.menu_service.dto.CategoryDto;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.service.helper.CategoryServiceHelper;
import com.devx.menu_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
public class CategoryServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final CategoryServiceHelper categoryServiceHelper;

    @Autowired
    public CategoryServiceIntegration(Scheduler jdbcScheduler, CategoryServiceHelper categoryServiceHelper) {
        this.jdbcScheduler = jdbcScheduler;
        this.categoryServiceHelper = categoryServiceHelper;
    }

    public Mono<CategoryDto> addCategory(Category category) {
        return Mono.fromCallable(() -> {
            Category savedCategory = categoryServiceHelper.addCategory(category);
            return AppUtils.CategoryUtils.entityToDto(savedCategory);
        }).subscribeOn(jdbcScheduler);
    }

    public Flux<CategoryDto> getAllCategories() {
        return Flux.fromIterable(categoryServiceHelper.getAllCategories())
                .map(AppUtils.CategoryUtils::entityToDto)
                .subscribeOn(jdbcScheduler);
    }
}

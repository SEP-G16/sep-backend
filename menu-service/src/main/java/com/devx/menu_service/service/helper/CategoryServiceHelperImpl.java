package com.devx.menu_service.service.helper;

import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.exception.CategoryNotFoundException;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryServiceHelperImpl implements CategoryServiceHelper {

    protected final CategoryRepository categoryRepository;

    public CategoryServiceHelperImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(Category category) {
        if (category.getId() == null && category.getName() == null) {
            throw new NullFieldException("Both ID and name cannot be null for an Category");
        }

        // If ID is null, try to find by name or save new Category
        if (category.getId() == null) {
            return categoryRepository.findByName(category.getName())
                    .orElseGet(() -> categoryRepository.save(category));
        }

        // If ID is present, check for Category by ID
        Category existingCategory = categoryRepository.findById(category.getId())
                .orElseThrow(() -> new CategoryNotFoundException("Category Not Found"));

        // If name is null, return the existing Category
        if (category.getName() == null) {
            return existingCategory;
        }

        // If names match, return the existing Category, else throw an exception
        if (!category.getName().equals(existingCategory.getName())) {
            throw new BadRequestException("Category Details Mismatch");
        }
        return existingCategory;
    }

    @Override
    public List<Category> getAllCategories() {
        return null;
    }
}

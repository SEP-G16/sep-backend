package com.devx.menu_service.service.helper;

import com.devx.menu_service.model.Category;

import java.util.List;

public interface CategoryServiceHelper {
    Category addCategory(Category category);

    List<Category> getAllCategories();
}

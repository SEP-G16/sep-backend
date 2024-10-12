package com.devx.menu_service.config;

import com.devx.menu_service.repository.AddOnRepository;
import com.devx.menu_service.repository.CategoryRepository;
import com.devx.menu_service.service.helper.AddOnServiceHelper;
import com.devx.menu_service.service.helper.AddOnServiceHelperImpl;
import com.devx.menu_service.service.helper.CategoryServiceHelper;
import com.devx.menu_service.service.helper.CategoryServiceHelperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public AddOnServiceHelper addOnServiceHelper(AddOnRepository addOnRepository) {
        return new AddOnServiceHelperImpl(addOnRepository);
    }

    @Bean
    public CategoryServiceHelper categoryServiceHelper(CategoryRepository categoryRepository) {
        return new CategoryServiceHelperImpl(categoryRepository);
    }
}

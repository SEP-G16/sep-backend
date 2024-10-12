package com.devx.menu_service.utils;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.dto.CategoryDto;
import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.dto.request.AddMenuItemRequestBody;
import com.devx.menu_service.enums.MenuItemStatus;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.model.MenuItem;

public class AppUtils {
    private AppUtils() {
    }

    public static class MenuUtils {
        public static MenuItem dtoToEntity(MenuItemDto menuItemDto) {
            MenuItem menuItem = new MenuItem();
            menuItem.setName(menuItemDto.getName());
            menuItem.setLongDescription(menuItemDto.getLongDescription());
            menuItem.setShortDescription(menuItemDto.getShortDescription());
            menuItem.setCategory(CategoryUtils.dtoToEntity(menuItemDto.getCategory()));
            menuItem.setPrice(menuItemDto.getPrice());
            menuItem.setIngredients(menuItemDto.getIngredients());
            menuItem.setAddOns((long) menuItemDto.getAddOns().size() > 0 ? menuItemDto.getAddOns().stream().map(AddOnUtils::dtoToEntity).toList() : null);
            menuItem.setImageUrl(menuItemDto.getImageUrl());
            menuItem.setCuisine(menuItemDto.getCuisine());
            menuItem.setTags(menuItemDto.getTags());
            menuItem.setStatus(menuItemDto.getStatus());
            return menuItem;
        }

        public static MenuItemDto entityToDto(MenuItem menuItem) {
            MenuItemDto menuItemDto = new MenuItemDto();
            menuItemDto.setId(menuItem.getId());
            menuItemDto.setName(menuItem.getName());
            menuItemDto.setLongDescription(menuItem.getLongDescription());
            menuItemDto.setShortDescription(menuItem.getShortDescription());
            menuItemDto.setCategory(CategoryUtils.entityToDto(menuItem.getCategory()));
            menuItemDto.setPrice(menuItem.getPrice());
            menuItemDto.setIngredients(menuItem.getIngredients());
            menuItemDto.setAddOns(menuItem.getAddOns().stream().map(AddOnUtils::entityToDto).toList());
            menuItemDto.setImageUrl(menuItem.getImageUrl());
            menuItemDto.setCuisine(menuItem.getCuisine());
            menuItemDto.setTags(menuItem.getTags());
            menuItemDto.setStatus(menuItem.getStatus());
            return menuItemDto;
        }
    }

    public static class AddOnUtils {
        private AddOnUtils() {
        }

        public static AddOn dtoToEntity(AddOnDto addOnDto) {
            AddOn addOn = new AddOn();
            addOn.setId(addOnDto.getId());
            addOn.setName(addOnDto.getName());
            addOn.setPrice(addOnDto.getPrice());
            return addOn;
        }

        public static AddOnDto entityToDto(AddOn addOn) {
            AddOnDto addOnDto = new AddOnDto();
            addOnDto.setId(addOn.getId());
            addOnDto.setName(addOn.getName());
            addOnDto.setPrice(addOn.getPrice());
            return addOnDto;
        }
    }

    public static class CategoryUtils {
        public static Category dtoToEntity(CategoryDto categoryDto) {
            Category category = new Category();
            category.setId(categoryDto.getId());
            category.setName(categoryDto.getName());
            return category;
        }

        public static CategoryDto entityToDto(Category category) {
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(category.getId());
            categoryDto.setName(category.getName());
            return categoryDto;
        }
    }
}

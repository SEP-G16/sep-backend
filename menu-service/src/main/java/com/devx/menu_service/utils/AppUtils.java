package com.devx.menu_service.utils;

import com.devx.menu_service.dto.request.AddMenuItemRequestBody;
import com.devx.menu_service.enums.MenuItemStatus;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.model.MenuItem;

public class AppUtils {
    private AppUtils(){}

    public static MenuItem MenuItemFromAddMenuItemRequestBody(AddMenuItemRequestBody addMenuItemRequestBody){
        MenuItem menuItem = new MenuItem();
        menuItem.setName(addMenuItemRequestBody.getName());
        menuItem.setLongDescription(addMenuItemRequestBody.getLongDescription());
        menuItem.setShortDescription(addMenuItemRequestBody.getShortDescription());
        menuItem.setCategory(Category.builder().id(addMenuItemRequestBody.getCategoryId()).build());
        menuItem.setPrice(addMenuItemRequestBody.getPrice());
        menuItem.setIngredients(addMenuItemRequestBody.getIngredients());
        menuItem.setAddOns(addMenuItemRequestBody.getAddOns().stream().map(addOnDto -> {
            AddOn addOn = new AddOn();
            addOn.setId(addOnDto.getId());
            addOn.setName(addOnDto.getName());
            addOn.setPrice(addOnDto.getPrice());
            return addOn;
        }).toList());
        menuItem.setImageUrl(addMenuItemRequestBody.getImageUrl());
        menuItem.setCuisine(addMenuItemRequestBody.getCuisine());
        menuItem.setTags(addMenuItemRequestBody.getTags());
        menuItem.setStatus(MenuItemStatus.InStock);
        return menuItem;
    }
}

package com.devx.menu_service;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.dto.CategoryDto;
import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.repository.CategoryRepository;
import com.devx.menu_service.service.MenuService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class MenuServiceIntegrationTests extends BaseIntegrationTestConfiguration{

    private final MenuService menuService;

    @Autowired
    public MenuServiceIntegrationTests(CategoryRepository categoryRepository, MenuService menuService) {
        this.menuService = menuService;
    }

    @Test
    void testMenuItemInsertBehaviourWithSameAddOns()
    {

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Main Course");

        AddOnDto addOn1 = new AddOnDto();
        addOn1.setName("Extra Sauce");
        addOn1.setPrice(1.49);

        AddOnDto addOn2 = new AddOnDto();
        addOn2.setName("Extra Cheese");
        addOn2.setPrice(2.99);

        List<AddOnDto> addOns = List.of(addOn1, addOn2);

        MenuItemDto menuItem1 = new MenuItemDto();
        menuItem1.setName("Cheese Pizza");
        menuItem1.setPrice(10.99);
        menuItem1.setCategory(categoryDto);
        menuItem1.setAddOns(addOns);
        menuItem1.setCuisine("Italian");
        menuItem1.setIngredients(Arrays.asList("Cheese", "Tomato Sauce"));
        menuItem1.setShortDescription("Cheese Pizza");
        menuItem1.setLongDescription("Cheese Pizza");
        menuItem1.setTags(Arrays.asList("Cheese", "Pizza"));
        menuItem1.setImageUrl("https://www.google.com");

        MenuItemDto menuItem2 = new MenuItemDto();
        menuItem2.setName("Chicken Pizza");
        menuItem2.setPrice(10.99);
        menuItem2.setCategory(categoryDto);
        menuItem2.setAddOns(addOns);
        menuItem2.setCuisine("Italian");
        menuItem2.setIngredients(Arrays.asList("Chicken", "Garlic"));
        menuItem2.setShortDescription("Chicken Pizza");
        menuItem2.setLongDescription("Chicken Pizza");
        menuItem2.setTags(Arrays.asList("Chicken", "Pizza"));
        menuItem2.setImageUrl("https://www.google.com");

        MenuItemDto savedMenuItemDto1 = menuService.addMenuItem(menuItem1).block();
        MenuItemDto savedMenuItemDto2 = menuService.addMenuItem(menuItem2).block();

        assert savedMenuItemDto1 != null;
        assert savedMenuItemDto2 != null;

        assert savedMenuItemDto1.getId() != null;
        assert savedMenuItemDto2.getId() != null;

        assert !savedMenuItemDto1.getId().equals(savedMenuItemDto2.getId());

        assert savedMenuItemDto1.getAddOns().size() == 2;
        assert savedMenuItemDto2.getAddOns().size() == 2;

        assert savedMenuItemDto1.getAddOns().get(0).getId() != null;
        assert savedMenuItemDto1.getAddOns().get(1).getId() != null;
        assert savedMenuItemDto2.getAddOns().get(0).getId() != null;
        assert savedMenuItemDto2.getAddOns().get(1).getId() != null;

        assert !savedMenuItemDto1.getAddOns().get(0).getId().equals(savedMenuItemDto1.getAddOns().get(1).getId());
        assert !savedMenuItemDto2.getAddOns().get(0).getId().equals(savedMenuItemDto2.getAddOns().get(1).getId());
    }
}

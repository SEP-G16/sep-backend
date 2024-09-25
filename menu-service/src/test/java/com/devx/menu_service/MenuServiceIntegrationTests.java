package com.devx.menu_service;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.dto.request.AddMenuItemRequestBody;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.repository.CategoryRepository;
import com.devx.menu_service.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MenuServiceIntegrationTests extends BaseIntegrationTestConfiguration{

    private final CategoryRepository categoryRepository;
    private final MenuService menuService;

    @Autowired
    public MenuServiceIntegrationTests(CategoryRepository categoryRepository, MenuService menuService) {
        this.categoryRepository = categoryRepository;
        this.menuService = menuService;
    }

    @Test
    void testMenuItemInsertBehaviourWithSameAddOns()
    {

        Category savedCategory = categoryRepository.save(Category.builder().name("Pizza").build());

        AddOnDto addOn1 = new AddOnDto();
        addOn1.setName("Extra Sauce");
        addOn1.setPrice(1.49);

        AddOnDto addOn2 = new AddOnDto();
        addOn2.setName("Extra Cheese");
        addOn2.setPrice(2.99);

        List<AddOnDto> addOns = List.of(addOn1, addOn2);

        AddMenuItemRequestBody menuItem1 = new AddMenuItemRequestBody();
        menuItem1.setName("Cheese Pizza");
        menuItem1.setPrice(10.99);
        menuItem1.setCategoryId(savedCategory.getId());
        menuItem1.setAddOns(addOns);
        menuItem1.setCuisine("Italian");
        menuItem1.setIngredients(Arrays.asList("Cheese", "Tomato Sauce"));
        menuItem1.setShortDescription("Cheese Pizza");
        menuItem1.setLongDescription("Cheese Pizza");
        menuItem1.setTags(Arrays.asList("Cheese", "Pizza"));
        menuItem1.setImageUrl("https://www.google.com");

        AddMenuItemRequestBody menuItem2 = new AddMenuItemRequestBody();
        menuItem2.setName("Chicken Pizza");
        menuItem2.setPrice(10.99);
        menuItem2.setCategoryId(savedCategory.getId());
        menuItem2.setAddOns(addOns);
        menuItem2.setCuisine("Italian");
        menuItem2.setIngredients(Arrays.asList("Chicken", "Garlic"));
        menuItem2.setShortDescription("Chicken Pizza");
        menuItem2.setLongDescription("Chicken Pizza");
        menuItem2.setTags(Arrays.asList("Chicken", "Pizza"));
        menuItem2.setImageUrl("https://www.google.com");

        ResponseEntity<Mono<MenuItem>> res1 = menuService.addMenuItem(menuItem1);
        ResponseEntity<Mono<MenuItem>> res2 = menuService.addMenuItem(menuItem2);

        MenuItem savedMenuItem1 = Objects.requireNonNull(res1.getBody()).block();
        MenuItem savedMenuItem2 = Objects.requireNonNull(res2.getBody()).block();

        assert savedMenuItem1 != null;
        assert savedMenuItem2 != null;

        assert savedMenuItem1.getId() != null;
        assert savedMenuItem2.getId() != null;

        assert !savedMenuItem1.getId().equals(savedMenuItem2.getId());

        assert savedMenuItem1.getAddOns().size() == 2;
        assert savedMenuItem2.getAddOns().size() == 2;

        assert savedMenuItem1.getAddOns().get(0).getId() != null;
        assert savedMenuItem1.getAddOns().get(1).getId() != null;
        assert savedMenuItem2.getAddOns().get(0).getId() != null;
        assert savedMenuItem2.getAddOns().get(1).getId() != null;

        assert !savedMenuItem1.getAddOns().get(0).getId().equals(savedMenuItem1.getAddOns().get(1).getId());
        assert !savedMenuItem2.getAddOns().get(0).getId().equals(savedMenuItem2.getAddOns().get(1).getId());
    }
}

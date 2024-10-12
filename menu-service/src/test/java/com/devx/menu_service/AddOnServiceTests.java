package com.devx.menu_service;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.service.AddOnService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AddOnServiceTests extends BaseIntegrationTestConfiguration{

    @Autowired
    private AddOnService addOnService;

    @Test
    void duplicateAddOnInsertionTest()
    {
        AddOnDto addOn1 = new AddOnDto();
        addOn1.setName("Extra Sauce");
        addOn1.setPrice(1.49);

        AddOnDto addOn2 = new AddOnDto();
        addOn2.setName("Extra Sauce");
        addOn2.setPrice(1.49);

        AddOnDto savedAddOnDto1 = addOnService.addAddOn(addOn1).block();
        AddOnDto savedAddOnDto2 = addOnService.addAddOn(addOn2).block();

        assert savedAddOnDto1 != null;
        assert savedAddOnDto2 != null;
        Assertions.assertEquals(savedAddOnDto1.getId(), savedAddOnDto2.getId());
    }
}

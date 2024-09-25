package com.devx.menu_service;

import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.service.AddOnService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Objects;

public class AddOnServiceTests extends BaseIntegrationTestConfiguration{

    @Autowired
    private AddOnService addOnService;

    @Test
    void duplicateAddOnInsertionTest()
    {
        AddOn addOn1 = new AddOn();
        addOn1.setName("Extra Sauce");
        addOn1.setPrice(1.49);

        AddOn addOn2 = new AddOn();
        addOn2.setName("Extra Sauce");
        addOn2.setPrice(1.49);

        ResponseEntity<Mono<AddOn>> addOnRes1 = addOnService.addAddOn(addOn1);
        ResponseEntity<Mono<AddOn>> addOnRes2 = addOnService.addAddOn(addOn2);

        AddOn savedAddOn1 = Objects.requireNonNull(addOnRes1.getBody()).block();
        AddOn savedAddOn2 = Objects.requireNonNull(addOnRes2.getBody()).block();

        assert savedAddOn1 != null;
        assert savedAddOn2 != null;
        Assertions.assertEquals(savedAddOn1.getId(), savedAddOn2.getId());
    }
}

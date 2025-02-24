package com.devx.menu_service;

import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.repository.AddOnRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public class AddOnRepoTests extends BaseIntegrationTestConfiguration{

    @Autowired
    private AddOnRepository addOnRepository;

    public static final Logger LOG = LoggerFactory.getLogger(AddOnRepoTests.class);

    @Test
    void insertionTest()
    {
        AddOn addOn1 = new AddOn();
        addOn1.setName("Extra Sauce");
        addOn1.setPrice(1.49);

        AddOn addOn2 = new AddOn();
        addOn2.setName("Extra Sauce");
        addOn2.setPrice(1.49);

        AddOn savedAddOn = addOnRepository.save(addOn1);
        Assertions.assertThat(savedAddOn.getId()).isNotNull();
        Assertions.assertThat(savedAddOn.getName()).isEqualTo("Extra Sauce");
        Assertions.assertThat(savedAddOn.getPrice()).isEqualTo(1.49);

        AddOn secondSavedAddOn = addOnRepository.save(addOn2);
        Assertions.assertThat(secondSavedAddOn.getId()).isNotNull();
        Assertions.assertThat(secondSavedAddOn.getName()).isEqualTo("Extra Sauce");
        Assertions.assertThat(secondSavedAddOn.getPrice()).isEqualTo(1.49);

        Assertions.assertThat(savedAddOn.getId()).isNotEqualTo(secondSavedAddOn.getId());
    }

    @AfterEach
    void afterEach(){
        addOnRepository.deleteAll();
    }
}

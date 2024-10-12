package com.devx.menu_service.service;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.service.integration.AddOnServiceIntegration;
import com.devx.menu_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AddOnService{

    private final AddOnServiceIntegration addOnServiceIntegration;

    @Autowired
    public AddOnService(AddOnServiceIntegration addOnServiceIntegration) {
        this.addOnServiceIntegration = addOnServiceIntegration;
    }

    public Mono<AddOnDto> addAddOn(AddOnDto addOnDto) {
        AddOn addOn = AppUtils.AddOnUtils.dtoToEntity(addOnDto);
        return addOnServiceIntegration.addAddOn(addOn);
    }

    public Flux<AddOnDto> getAllAddOns() {
        return addOnServiceIntegration.getAllAddOns();
    }
}

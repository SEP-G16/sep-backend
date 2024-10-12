package com.devx.menu_service.service.integration;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.service.helper.AddOnServiceHelper;
import com.devx.menu_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
public class AddOnServiceIntegration {

    private final Scheduler jdbcScheduler;
    private final AddOnServiceHelper addOnServiceHelper;

    @Autowired
    public AddOnServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, AddOnServiceHelper addOnServiceHelper) {
        this.jdbcScheduler = jdbcScheduler;
        this.addOnServiceHelper = addOnServiceHelper;
    }

    public Mono<AddOnDto> addAddOn(AddOn addOn) {
        return Mono.fromCallable(() -> {
            AddOn savedAddOn = addOnServiceHelper.addAddOn(addOn);
            return AppUtils.AddOnUtils.entityToDto(savedAddOn);
        }).subscribeOn(jdbcScheduler);
    }

    public Flux<AddOnDto> getAllAddOns() {
        return Flux.fromIterable(addOnServiceHelper.getAllAddOns())
                .map(AppUtils.AddOnUtils::entityToDto)
                .subscribeOn(jdbcScheduler);
    }
}

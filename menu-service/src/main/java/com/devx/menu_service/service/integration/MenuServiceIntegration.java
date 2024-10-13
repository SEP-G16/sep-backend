package com.devx.menu_service.service.integration;

import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.enums.MenuItemStatus;
import com.devx.menu_service.exception.MenuItemNotFoundException;
import com.devx.menu_service.message.MessageSender;
import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.repository.MenuItemRepository;
import com.devx.menu_service.service.helper.AddOnServiceHelper;
import com.devx.menu_service.service.helper.CategoryServiceHelper;
import com.devx.menu_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MenuServiceIntegration {
    private final MenuItemRepository menuItemRepository;
    private final AddOnServiceHelper addOnServiceHelper;
    private final CategoryServiceHelper categoryServiceHelper;

    private final MessageSender messageSender;

    private final Scheduler jdbcScheduler;

    public MenuServiceIntegration(MenuItemRepository menuItemRepository, AddOnServiceHelper addOnServiceHelper, CategoryServiceHelper categoryServiceHelper, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler, MessageSender messageSender) {
        this.menuItemRepository = menuItemRepository;
        this.addOnServiceHelper = addOnServiceHelper;
        this.categoryServiceHelper = categoryServiceHelper;
        this.jdbcScheduler = jdbcScheduler;
        this.messageSender = messageSender;
    }

    private List<AddOn> getOrInsertAddOns(List<AddOn> addOns){
        List<AddOn> savedAddOns = new ArrayList<>();
        for(AddOn addOn : addOns){
            savedAddOns.add(addOnServiceHelper.addAddOn(addOn));
        }
        return savedAddOns;
    }

    private Category getOrInsertCategory(Category category){
        return categoryServiceHelper.addCategory(category);
    }

    private MenuItem addMenuItemInternal(MenuItem menuItem){
        MenuItem saved = menuItemRepository.save(menuItem);
        messageSender.sendAddMenuItemMessage(saved);
        return saved;
    }

    public Mono<MenuItemDto> addMenuItem(MenuItem menuItem){
        List<AddOn> savedAddOns = getOrInsertAddOns(menuItem.getAddOns());
        Category savedCategory = getOrInsertCategory(menuItem.getCategory());
        menuItem.setAddOns(savedAddOns);
        menuItem.setCategory(savedCategory);
        return Mono.fromCallable(() -> {
            MenuItem savedMenuItem = addMenuItemInternal(menuItem);
            return AppUtils.MenuUtils.entityToDto(savedMenuItem);
        }).subscribeOn(jdbcScheduler);
    }

    private List<MenuItem> getAllMenuItemsInternal(){
        return menuItemRepository.findAll();
    }

    public Flux<MenuItemDto> getAllMenuItems() {
        return Flux.fromIterable(getAllMenuItemsInternal()).map(AppUtils.MenuUtils::entityToDto).subscribeOn(jdbcScheduler);
    }

    private MenuItem updateMenuItemStatusInternal(Long id, MenuItemStatus status) {
        Optional<MenuItem> existingMenuItem = menuItemRepository.findById(id);
        if(existingMenuItem.isPresent()){
            MenuItem menuItem = existingMenuItem.get();
            menuItem.setStatus(status);
            return menuItemRepository.save(menuItem);
        }
        else {
            throw new MenuItemNotFoundException("Menu item with id " + id + " not found");
        }
    }

    public Mono<MenuItemDto> updateMenuItemStatus(Long id, MenuItemStatus status) {
        return Mono.fromCallable(() -> {
            MenuItem updatedMenuItem = updateMenuItemStatusInternal(id, status);
            return AppUtils.MenuUtils.entityToDto(updatedMenuItem);
        }).subscribeOn(jdbcScheduler);
    }

    public void emitToOrderServiceInternal() {
        menuItemRepository.findAll().forEach(messageSender::sendAddMenuItemMessage);
    }

    public Mono<Void> emitToOrderService() {
        emitToOrderServiceInternal();
        return Mono.empty();
    }
}

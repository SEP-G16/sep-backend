package com.devx.menu_service.service;

import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.dto.request.UpdateMenuItemStatusRequestBody;
import com.devx.menu_service.enums.MenuItemStatus;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.service.integration.MenuServiceIntegration;
import com.devx.menu_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MenuService{

    MenuServiceIntegration menuServiceIntegration;

    @Autowired
    public MenuService(MenuServiceIntegration menuServiceIntegration)
    {
        this.menuServiceIntegration = menuServiceIntegration;
    }

    public Mono<MenuItemDto> addMenuItem(MenuItemDto menuItemDto) {
        MenuItem menuItem = AppUtils.MenuUtils.dtoToEntity(menuItemDto);
        menuItem.setStatus(MenuItemStatus.InStock);
        return menuServiceIntegration.addMenuItem(menuItem);
    }

    public Flux<MenuItemDto> getAllMenuItems() {
        return menuServiceIntegration.getAllMenuItems();
    }

    public Mono<MenuItemDto> updateMenuItemStatus(UpdateMenuItemStatusRequestBody reqBody) {
        Long id = reqBody.getId();
        MenuItemStatus status = reqBody.getStatus();
        return menuServiceIntegration.updateMenuItemStatus(id, status);
    }

    public Mono<Void> emitToOrderService() {
        return menuServiceIntegration.emitToOrderService();
    }
}

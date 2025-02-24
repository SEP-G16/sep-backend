package com.devx.menu_service.controller;

import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.dto.request.UpdateMenuItemStatusRequestBody;
import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public static Logger LOG = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<MenuItemDto>> getAll() {
        try {
            return ResponseEntity.ok(menuService.getAllMenuItems());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching all menu items", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<MenuItemDto>> add(@RequestBody MenuItemDto menuItemDto) {
        try {
            if (menuItemDto.hasNullFields()) {
                throw new NullFieldException("Fields of menu item cannot be null");
            }
            return ResponseEntity.ok(menuService.addMenuItem(menuItemDto));
        } catch (NullFieldException | BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while adding menu item", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<Mono<MenuItemDto>> updateStatus(@RequestBody UpdateMenuItemStatusRequestBody reqBody) {
        try {
            if (reqBody.getId() == null || reqBody.getStatus() == null) {
                throw new NullFieldException("Fields of id and/or status cannot be null");
            }
            return ResponseEntity.ok(menuService.updateMenuItemStatus(reqBody));
        } catch (NullFieldException | BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while updating menu item status", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @PostMapping("/emit-to-order-service")
    public ResponseEntity<Mono<Void>> emitToOrderService() {
        try {
            return ResponseEntity.ok(menuService.emitToOrderService());
        } catch (NullFieldException | BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while emitting menu item to order service", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}

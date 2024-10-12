package com.devx.menu_service.controller;

import com.devx.menu_service.dto.MenuItemDto;
import com.devx.menu_service.dto.request.AddMenuItemRequestBody;
import com.devx.menu_service.exception.BadRequestException;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.model.MenuItem;
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
}

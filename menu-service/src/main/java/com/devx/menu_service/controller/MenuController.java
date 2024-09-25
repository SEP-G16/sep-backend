package com.devx.menu_service.controller;

import com.devx.menu_service.dto.request.AddMenuItemRequestBody;
import com.devx.menu_service.model.MenuItem;
import com.devx.menu_service.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<MenuItem>> getAll() {
        return menuService.getAllMenuItems();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<MenuItem>> add(@RequestBody AddMenuItemRequestBody addMenuItemRequestBody) {
        return menuService.addMenuItem(addMenuItemRequestBody);
    }
}

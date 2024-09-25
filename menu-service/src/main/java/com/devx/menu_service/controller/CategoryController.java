package com.devx.menu_service.controller;

import com.devx.menu_service.model.Category;
import com.devx.menu_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Category>> getAll(){
        return categoryService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<Category>> add(@RequestBody Category category){
        return categoryService.addCategory(category);
    }
}

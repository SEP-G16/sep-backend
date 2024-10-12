package com.devx.menu_service.controller;

import com.devx.menu_service.dto.CategoryDto;
import com.devx.menu_service.model.Category;
import com.devx.menu_service.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu/category")
public class CategoryController {
    private final CategoryService categoryService;

    public static Logger LOG = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<CategoryDto>> getAll(){
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching all categories", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<CategoryDto>> add(@RequestBody CategoryDto categoryDto){
        try {
            return ResponseEntity.created(null).body(categoryService.addCategory(categoryDto));
        } catch (Exception e) {
            LOG.error("Error occurred while adding category", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}

package com.devx.menu_service.controller;

import com.devx.menu_service.model.AddOn;
import com.devx.menu_service.service.AddOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu/add-on")
public class AddOnController {

    private final AddOnService addOnService;

    @Autowired
    public AddOnController(AddOnService addOnService) {
        this.addOnService = addOnService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<AddOn>> addOn(@RequestBody AddOn addOn) {
        return addOnService.addAddOn(addOn);
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<AddOn>> getAll() {
        return addOnService.getAllAddOns();
    }


}

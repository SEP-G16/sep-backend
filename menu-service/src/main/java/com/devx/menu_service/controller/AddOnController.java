package com.devx.menu_service.controller;

import com.devx.menu_service.dto.AddOnDto;
import com.devx.menu_service.exception.NullFieldException;
import com.devx.menu_service.service.AddOnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/menu/add-on")
public class AddOnController {

    private final AddOnService addOnService;


    public static Logger LOG = LoggerFactory.getLogger(AddOnController.class);

    @Autowired
    public AddOnController(AddOnService addOnService) {
        this.addOnService = addOnService;
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<AddOnDto>> addOn(@RequestBody AddOnDto addOnDto) {
        try {

            if (addOnDto.hasNullFields()) {
                throw new NullFieldException("Fields of add-on cannot be null");
            }
            return ResponseEntity.created(null).body(addOnService.addAddOn(addOnDto));
        } catch (NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while adding add-on", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<AddOnDto>> getAll() {
        try {
            return ResponseEntity.ok(addOnService.getAllAddOns());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching all add-ons", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }


}

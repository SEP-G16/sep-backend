package com.devx.table_reservation_service.controller;

import com.devx.table_reservation_service.dto.CheckTableAvailabilityDto;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/table")
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<RestaurantTable>> getAllTables() {
        return tableService.getAllTables();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<RestaurantTable>> addTable(@RequestBody RestaurantTable restaurantTable) {
        return tableService.addTable(restaurantTable);
    }

    @GetMapping("/available")
    public ResponseEntity<Flux<RestaurantTable>> getAvailableTables(CheckTableAvailabilityDto dto) {
        return tableService.getAvailableTables(dto);
    }
}

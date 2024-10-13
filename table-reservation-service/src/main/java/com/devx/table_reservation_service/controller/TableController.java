package com.devx.table_reservation_service.controller;

import com.devx.table_reservation_service.dto.CheckTableAvailabilityDto;
import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.exception.BadRequestException;
import com.devx.table_reservation_service.exception.NullFieldException;
import com.devx.table_reservation_service.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/table")
public class TableController {
    private final TableService tableService;

    public static final Logger LOG = LoggerFactory.getLogger(TableController.class);

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<RestaurantTableDto>> getAllTables() {
        try {
            return ResponseEntity.ok().body(tableService.getAllTables());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching all tables: ", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<RestaurantTableDto>> addTable(@RequestBody RestaurantTableDto restaurantTableDto) {
        try {
            return ResponseEntity.created(null).body(tableService.addTable(restaurantTableDto));
        } catch (NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while adding table: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<Flux<RestaurantTableDto>> getAvailableTables(CheckTableAvailabilityDto dto) {
        try {
            return ResponseEntity.ok().body(tableService.getAvailableTables(dto));
        } catch (NullFieldException e) {
            return ResponseEntity.badRequest().body(Flux.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while fetching available tables: ", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mono<RestaurantTableDto>> getTableById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(tableService.getTableById(id));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while fetching table by id: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}

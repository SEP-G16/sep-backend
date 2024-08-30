package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.dto.CheckTableAvailabilityDto;
import com.devx.table_reservation_service.exception.KeyNotFoundException;
import com.devx.table_reservation_service.exception.StoredProcedureCallException;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.AvailableTablesRepository;
import com.devx.table_reservation_service.repository.TableRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;

@Service
public class TableService {
    private final TableRepository tableRepository;
    private final Scheduler jdbcScheduler;
    private final AvailableTablesRepository availableTablesRepository;

    public static Logger LOG = LoggerFactory.getLogger(TableService.class);

    @Autowired
    public TableService(
            @Qualifier("jdbcScheduler") Scheduler jdbcScheduler,
            TableRepository tableRepository,
            AvailableTablesRepository availableTablesRepository
    ) {
        this.tableRepository = tableRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.availableTablesRepository = availableTablesRepository;
    }

    private Mono<RestaurantTable> addTableToDatabase(RestaurantTable restaurantTable) {
        return Mono.fromCallable(() -> tableRepository.save(restaurantTable)).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Mono<RestaurantTable>> addTable(RestaurantTable restaurantTable) {
        try {
            return ResponseEntity.ok(addTableToDatabase(restaurantTable));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private Flux<RestaurantTable> getAllTablesFromDatabase() {
        return Flux.fromIterable(tableRepository.findAll()).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Flux<RestaurantTable>> getAllTables() {
        try {
            return ResponseEntity.ok(getAllTablesFromDatabase());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private Flux<RestaurantTable> getAvailableTablesFromDatabase(LocalDate selectedDate, int timeSlotStart, int timeSlotEnd) throws StoredProcedureCallException, KeyNotFoundException {
        return Flux.fromIterable(availableTablesRepository
                .getAvailableRoomCount(
                        selectedDate,
                        timeSlotStart,
                        timeSlotEnd)).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Flux<RestaurantTable>> getAvailableTables(CheckTableAvailabilityDto dto) {
        try {
            return ResponseEntity.ok(getAvailableTablesFromDatabase(dto.getSelectedDate(), dto.getTimeSlotStart(), dto.getTimeSlotEnd()));
        } catch (KeyNotFoundException | StoredProcedureCallException e){
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

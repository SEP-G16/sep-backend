package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.dto.CheckTableAvailabilityDto;
import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.exception.KeyNotFoundException;
import com.devx.table_reservation_service.exception.NullFieldException;
import com.devx.table_reservation_service.exception.StoredProcedureCallException;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.AvailableTablesRepository;
import com.devx.table_reservation_service.repository.TableRepository;
import com.devx.table_reservation_service.utils.AppUtils;
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

    private final TableServiceIntegration tableServiceIntegration;

    @Autowired
    public TableService(
            TableServiceIntegration tableServiceIntegration) {
        this.tableServiceIntegration = tableServiceIntegration;
    }

    public Mono<RestaurantTableDto> addTable(RestaurantTableDto restaurantTableDto) {
        RestaurantTable table = AppUtils.RestaurantTableUtils.convertRestaurantTableDtoToRestaurantTableEntity(restaurantTableDto);
        return tableServiceIntegration.addTable(table);
    }

    public Flux<RestaurantTableDto> getAllTables() {
        return tableServiceIntegration.getAllTables();
    }

    public Flux<RestaurantTableDto> getAvailableTables(CheckTableAvailabilityDto dto) {
        if (dto.hasNullFields()) {
            throw new NullFieldException("Null fields found in request");
        }
        LocalDate selectedDate = dto.getSelectedDate();
        int timeSlotStart = dto.getTimeSlotStart();
        int timeSlotEnd = dto.getTimeSlotEnd();
        return tableServiceIntegration.getAvailableTables(selectedDate, timeSlotStart, timeSlotEnd);
    }
}

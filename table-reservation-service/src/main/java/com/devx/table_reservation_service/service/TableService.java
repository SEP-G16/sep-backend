package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.dto.CheckTableAvailabilityDto;
import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.exception.NullFieldException;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
        RestaurantTable table = AppUtils.RestaurantTableUtils.dtoToEntity(restaurantTableDto);
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

    public Mono<RestaurantTableDto> getTableById(Long id) {
        return tableServiceIntegration.getTableById(id);
    }
}

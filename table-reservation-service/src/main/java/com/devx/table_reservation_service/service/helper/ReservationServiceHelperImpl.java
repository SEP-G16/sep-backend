package com.devx.table_reservation_service.service.helper;

import com.devx.table_reservation_service.exception.BadRequestException;
import com.devx.table_reservation_service.exception.NullFieldException;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationServiceHelperImpl implements ReservationServiceHelper {

    private final TableRepository tableRepository;

    @Autowired
    public ReservationServiceHelperImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public RestaurantTable addTable(RestaurantTable table) {
        // Check if both ID and name are null
        if (table.getId() == null && table.getTableNo() == null) {
            throw new NullFieldException("Both ID and table no cannot be null for an RestaurantTable");
        }

        // If ID is null, try to find by name or save new RestaurantTable
        if (table.getId() == null) {
            return tableRepository.findByTableNo(table.getTableNo())
                    .orElseGet(() -> tableRepository.save(table));
        }

        // If ID is present, check for RestaurantTable by ID
        RestaurantTable existingRestaurantTable = tableRepository.findById(table.getId())
                .orElseThrow(() -> new RuntimeException("RestaurantTable Not Found"));

        // If name is null, return the existing RestaurantTable
        if (table.getTableNo() == null) {
            return existingRestaurantTable;
        }

        // If names match, return the existing RestaurantTable, else throw an exception
        if (!table.getTableNo().equals(existingRestaurantTable.getTableNo())) {
            throw new BadRequestException("RestaurantTable Details Mismatch");
        }
        return existingRestaurantTable;
    }
}

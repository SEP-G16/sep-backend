package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.dto.RestaurantTableDto;
import com.devx.table_reservation_service.exception.BadRequestException;
import com.devx.table_reservation_service.message.MessageSender;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.AvailableTablesRepository;
import com.devx.table_reservation_service.repository.TableRepository;
import com.devx.table_reservation_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.List;

@Component
public class TableServiceIntegration {

    private final Scheduler jdbcScheduler;

    private final TableRepository tableRepository;

    private final AvailableTablesRepository availableTablesRepository;

    private final MessageSender messageSender;

    public TableServiceIntegration(@Qualifier("jdbcScheduler") Scheduler jdbcScheduler, TableRepository tableRepository, AvailableTablesRepository availableTablesRepository, MessageSender messageSender) {
        this.jdbcScheduler = jdbcScheduler;
        this.tableRepository = tableRepository;
        this.availableTablesRepository = availableTablesRepository;
        this.messageSender = messageSender;
    }

    private RestaurantTable addTableInternal(RestaurantTable table) {
        RestaurantTable saved = tableRepository.save(table);
        messageSender.sendAddRestaurantTableMessage(saved);
        return saved;
    }


    public Mono<RestaurantTableDto> addTable(RestaurantTable table) {
        return Mono.fromCallable(() -> {
            RestaurantTable savedTable = addTableInternal(table);
            return AppUtils.RestaurantTableUtils.entityToDto(savedTable);
        }).subscribeOn(jdbcScheduler);
    }

    private List<RestaurantTable> getAllTablesInternal() {
        return tableRepository.findAll();
    }

    public Flux<RestaurantTableDto> getAllTables() {
        return Mono.fromCallable(() -> getAllTablesInternal().stream().map(AppUtils.RestaurantTableUtils::entityToDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    private List<RestaurantTable> getAvailableTablesInternal(LocalDate selectedDate, int timeSlotStart, int timeSlotEnd) {
        return availableTablesRepository.getAvailableTables(selectedDate, timeSlotStart, timeSlotEnd);

    }

    public Flux<RestaurantTableDto> getAvailableTables(LocalDate selectedDate, int timeSlotStart, int timeSlotEnd) {
        return Mono.fromCallable(() -> getAvailableTablesInternal(selectedDate, timeSlotStart, timeSlotEnd).stream().map(AppUtils.RestaurantTableUtils::entityToDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    private RestaurantTable getTableByIdInternal(Long id) {
        return tableRepository.findById(id).orElseThrow(() -> new BadRequestException("Table not found"));
    }

    public Mono<RestaurantTableDto> getTableById(Long id) {
        return Mono.fromCallable(() -> AppUtils.RestaurantTableUtils.entityToDto(getTableByIdInternal(id))).subscribeOn(jdbcScheduler);
    }

    private void emitToOrderServiceInternal() {
        tableRepository.findAll().forEach(messageSender::sendAddRestaurantTableMessage);
    }

    public Mono<Void> emitToOrderService() {
        emitToOrderServiceInternal();
        return Mono.empty();
    }
}

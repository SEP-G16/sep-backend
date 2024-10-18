package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.dto.ReservationDto;
import com.devx.table_reservation_service.exception.ReservationNotFoundException;
import com.devx.table_reservation_service.model.Reservation;
import com.devx.table_reservation_service.model.RestaurantTable;
import com.devx.table_reservation_service.repository.ReservationRepository;
import com.devx.table_reservation_service.service.helper.ReservationServiceHelper;
import com.devx.table_reservation_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationServiceIntegration {

    private final ReservationRepository reservationRepository;
    private final Scheduler jdbcScheduler;
    private final ReservationServiceHelper reservationServiceHelper;

    public ReservationServiceIntegration(ReservationRepository reservationRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler, ReservationServiceHelper reservationServiceHelper) {
        this.reservationRepository = reservationRepository;
        this.jdbcScheduler = jdbcScheduler;
        this.reservationServiceHelper = reservationServiceHelper;
    }

    private List<RestaurantTable> addTables(List<RestaurantTable> tables)
    {
        List<RestaurantTable> saved = new ArrayList<>();
        for(RestaurantTable table: tables)
        {
            saved.add(reservationServiceHelper.addTable(table));
        }
        return saved;
    }

    private Reservation addReservationInternal(Reservation reservation) {
        reservation.setRestaurantTableList(addTables(reservation.getRestaurantTableList()));
        System.out.println("Adding reservation: " + reservation);
        return reservationRepository.save(reservation);
    }

    public Mono<ReservationDto> addReservation(Reservation reservation) {
        return Mono.fromCallable(() -> {
            Reservation savedReservation = addReservationInternal(reservation);
            return AppUtils.ReservationUtils.entityToDto(savedReservation);
        }).subscribeOn(jdbcScheduler);
    }

    private List<Reservation> getAllReservationsInternal() {
        return reservationRepository.findAll();
    }

    public Flux<ReservationDto> getAllReservations() {
        return Mono.fromCallable(() -> getAllReservationsInternal().stream().map(AppUtils.ReservationUtils::entityToDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }

    private void cancelReservationInternal(Long reservationId) {
        Optional<Reservation> existingReservationOptional = reservationRepository.findById(reservationId);
        if (existingReservationOptional.isPresent()) {
            reservationRepository.deleteById(reservationId);
        } else {
            throw new ReservationNotFoundException("Reservation not found");
        }
    }

    public Mono<Void> cancelReservation(Long reservationId) {
        cancelReservationInternal(reservationId);
        return Mono.empty();
    }

    private List<Reservation> getReservationsByDateInternal(LocalDate date) {
        return reservationRepository.findByReservedDate(date);
    }

    public Flux<ReservationDto> getReservationsByDate(LocalDate date) {
        return Mono.fromCallable(() -> getReservationsByDateInternal(date).stream().map(AppUtils.ReservationUtils::entityToDto).toList()).flatMapMany(Flux::fromIterable).subscribeOn(jdbcScheduler);
    }
}

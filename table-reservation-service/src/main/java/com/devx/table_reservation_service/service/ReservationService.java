package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.dto.ReservationDto;
import com.devx.table_reservation_service.model.Reservation;
import com.devx.table_reservation_service.utils.AppUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
public class ReservationService {

    private final ReservationServiceIntegration reservationServiceIntegration;

    public static Logger LOG = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    public ReservationService(ReservationServiceIntegration reservationServiceIntegration) {
        this.reservationServiceIntegration = reservationServiceIntegration;
    }

    @Transactional
    public Mono<ReservationDto> addReservation(ReservationDto reservationDto) {
        Reservation reservation = AppUtils.ReservationUtils.dtoToEntity(reservationDto);
        return reservationServiceIntegration.addReservation(reservation);
    }

    public Flux<ReservationDto> getAllReservations() {
        return reservationServiceIntegration.getAllReservations();
    }

    public Mono<Void> cancelReservation(Long reservationId) {
        return reservationServiceIntegration.cancelReservation(reservationId);
    }

    public Flux<ReservationDto> getReservationsByDate(LocalDate date) {
        return reservationServiceIntegration.getReservationsByDate(date);
    }
}

package com.devx.table_reservation_service.service;

import com.devx.table_reservation_service.model.Reservation;
import com.devx.table_reservation_service.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final Scheduler jdbcScheduler;

    public static Logger LOG = LoggerFactory.getLogger(ReservationService.class);

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, @Qualifier("jdbcScheduler") Scheduler jdbcScheduler) {
        this.reservationRepository = reservationRepository;
        this.jdbcScheduler = jdbcScheduler;
    }

    private Mono<Reservation> addReservationToDatabase(Reservation reservation) {
        return Mono.just(reservationRepository.save(reservation)).subscribeOn(jdbcScheduler);
    }

    public ResponseEntity<Mono<Reservation>> addReservation(Reservation reservation) {
        try {
            return ResponseEntity.ok(addReservationToDatabase(reservation));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    private Flux<Reservation> getAllReservationsFromDatabase() {
        return Flux.fromIterable(reservationRepository.findAll());
    }

    public ResponseEntity<Flux<Reservation>> getAllReservations() {
        try {
            return ResponseEntity.ok(getAllReservationsFromDatabase());
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    public ResponseEntity<Mono<Void>> cancelReservation(Long reservationId) {
        try {
            reservationRepository.deleteById(reservationId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}

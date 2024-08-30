package com.devx.table_reservation_service.controller;

import com.devx.table_reservation_service.model.Reservation;
import com.devx.table_reservation_service.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("table/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Reservation>> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<Reservation>> addReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Mono<Void>> cancelReservation(@RequestBody Long reservationId) {
        return reservationService.cancelReservation(reservationId);
    }
}

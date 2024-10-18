package com.devx.table_reservation_service.controller;

import com.devx.table_reservation_service.dto.ReservationDto;
import com.devx.table_reservation_service.exception.BadRequestException;
import com.devx.table_reservation_service.exception.NullFieldException;
import com.devx.table_reservation_service.exception.ReservationNotFoundException;
import com.devx.table_reservation_service.service.ReservationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/table/reservation")
public class ReservationController {
    private final ReservationService reservationService;

    public static final Logger LOG = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<ReservationDto>> getAllReservations() {
        try {
            return ResponseEntity.ok().body(reservationService.getAllReservations());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching all reservations: ", e);
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<ReservationDto>> addReservation(@RequestBody ReservationDto reservationDto) {
        try {
            return ResponseEntity.created(null).body(reservationService.addReservation(reservationDto));
        } catch (NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while adding reservation: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Mono<Void>> cancelReservation(@RequestBody Long reservationId) {
        try {
            if (reservationId == null) {
                throw new BadRequestException("Reservation ID is required for cancelling reservation");
            }
            return ResponseEntity.ok().body(reservationService.cancelReservation(reservationId));
        } catch (BadRequestException | ReservationNotFoundException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("Error occurred while cancelling reservation: ", e);
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @GetMapping("/by-date")
    public ResponseEntity<Flux<ReservationDto>> getReservationsByDate(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return ResponseEntity.ok().body(reservationService.getReservationsByDate(date));
        } catch (Exception e) {
            LOG.error("Error occurred while fetching reservations by date: ", e);
            return ResponseEntity.internalServerError().body(Flux.error(new Exception(e.getMessage())));
        }
    }
}

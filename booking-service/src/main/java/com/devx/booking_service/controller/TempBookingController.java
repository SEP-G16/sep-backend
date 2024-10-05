package com.devx.booking_service.controller;

import com.devx.booking_service.dto.TempBookingDto;
import com.devx.booking_service.exception.NullFieldException;
import com.devx.booking_service.exception.RoomNotFoundException;
import com.devx.booking_service.exception.RoomTypeNotFoundException;
import com.devx.booking_service.exception.TempBookingNotFoundException;
import com.devx.booking_service.model.TempBooking;
import com.devx.booking_service.service.TempBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/booking/temp")
public class TempBookingController {

    private final TempBookingService tempBookingService;

    @Autowired
    public TempBookingController(TempBookingService tempBookingService) {
        this.tempBookingService = tempBookingService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<TempBookingDto>> getAllTempBookings() {
        try {
            return ResponseEntity.ok().body(tempBookingService.getAllReservations());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<TempBookingDto>> addTempBooking(@RequestBody TempBookingDto tempBookingDto)
    {
        try {
            return ResponseEntity.created(null).body(tempBookingService.addReservation(tempBookingDto));
        } catch (NullFieldException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Mono<Void>> removeTempBooking(@RequestBody Long tempBookingId) {
        try {
            return ResponseEntity.ok().body(tempBookingService.removeReservation(tempBookingId));
        }
        catch (TempBookingNotFoundException e)
        {
            return ResponseEntity.notFound().build();
        }
        catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}

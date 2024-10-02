package com.devx.booking_service.controller;

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
    public ResponseEntity<Flux<TempBooking>> getAllTempBookings() {
        return tempBookingService.getAll();
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<TempBooking>> addTempBooking(@RequestBody TempBooking tempBooking) //TODO:Create DTO for this
    {
        return tempBookingService.insert(tempBooking);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeTempBooking(@RequestBody Long tempBookingId) {
        return tempBookingService.removeTempBooking(tempBookingId);
    }
}

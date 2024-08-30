package com.devx.booking_service.controller;

import com.devx.booking_service.model.Booking;
import com.devx.booking_service.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/booking")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService)
    {
        this.bookingService = bookingService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<Booking>> getAllBookings()
    {
        return bookingService.getAll();
    }

    //TODO:Create DTO for this
    @PostMapping("/add")
    public ResponseEntity<Mono<Booking>> addBooking(@RequestBody Booking booking)
    {
        return bookingService.insert(booking);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeBooking(@RequestBody Long bookingId)
    {
        return bookingService.removeBooking(bookingId);
    }
}

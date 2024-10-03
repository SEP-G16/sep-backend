package com.devx.booking_service.controller;

import com.devx.booking_service.dto.BookingDto;
import com.devx.booking_service.exception.*;
import com.devx.booking_service.model.Booking;
import com.devx.booking_service.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/booking")
public class BookingController {

    public static final Logger LOG = LoggerFactory.getLogger(BookingController.class);
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/all")
    public ResponseEntity<Flux<BookingDto>> getAllBookings() {
        try {
            return ResponseEntity.ok().body(bookingService.getAllBookings());
        } catch (Exception e) {
            LOG.error("Error occurred while fetching bookings{}", e.getMessage());
            return ResponseEntity.internalServerError().body(Flux.error(e));
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Mono<BookingDto>> addBooking(@RequestBody BookingDto bookingDto) {
        try {
            return ResponseEntity.created(null).body(bookingService.addBooking(bookingDto));
        } catch (NullFieldException | NoRoomsSelectedException | RoomCountMismatchException |
                 RoomTypeNotFoundException | RoomNotFoundException | RoomTypeMismatchException e) {
            return ResponseEntity.badRequest().body(Mono.error(e));
        } catch (Exception e) {
            LOG.error("An unexpected error occurred while adding booking{}", e.getMessage());
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Mono<Void>> removeBooking(@RequestBody Long bookingId) {
        try{
            if(bookingId == null)
            {
                throw new BadRequestException("Booking Id cannot be null");
            }
            return ResponseEntity.ok().body(bookingService.removeBooking(bookingId));
        } catch (BadRequestException | BookingNotFoundException e)
        {
            return ResponseEntity.badRequest().body(Mono.error(e));
        }
        catch (Exception e) {
            LOG.error("An unexpected error occurred while removing booking{}", e.getMessage());
            return ResponseEntity.internalServerError().body(Mono.error(e));
        }
    }
}

package com.devx.booking_service.service;

import com.devx.booking_service.dto.BookingDto;
import com.devx.booking_service.model.Booking;
import com.devx.booking_service.service.integration.BookingServiceIntegration;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BookingService {

    private final BookingServiceIntegration bookingServiceIntegration;

    @Autowired
    public BookingService(
            BookingServiceIntegration bookingServiceIntegration
    ){
        this.bookingServiceIntegration = bookingServiceIntegration;
    }

    public Mono<BookingDto> addBooking(BookingDto bookingDto) {
        Booking booking = AppUtils.BookingUtils.convertDtoToEntity(bookingDto);
        return bookingServiceIntegration.addBooking(booking);
    }


    public Flux<BookingDto> getAllBookings() {
        return bookingServiceIntegration.getAllBookings();
    }

    public Mono<Void> removeBooking(Long bookingId) {
        return bookingServiceIntegration.removeBooking(bookingId);
    }
}

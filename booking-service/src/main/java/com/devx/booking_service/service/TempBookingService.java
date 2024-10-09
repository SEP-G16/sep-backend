package com.devx.booking_service.service;

import com.devx.booking_service.dto.TempBookingDto;
import com.devx.booking_service.model.TempBooking;
import com.devx.booking_service.service.integration.TempBookingServiceIntegration;
import com.devx.booking_service.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TempBookingService {
    private final TempBookingServiceIntegration tempBookingServiceIntegration;

    @Autowired
    public TempBookingService(TempBookingServiceIntegration tempBookingServiceIntegration) {
        this.tempBookingServiceIntegration = tempBookingServiceIntegration;
    }

    public Mono<TempBookingDto> addReservation(TempBookingDto tempBookingDto) {
        TempBooking tempBooking = AppUtils.TempBookingUtils.convertDtoToEntity(tempBookingDto);
        return tempBookingServiceIntegration.createReservation(tempBooking);
    }

    public Flux<TempBookingDto> getAllReservations() {
        return tempBookingServiceIntegration.getAllReservations();
    }

    public Mono<Void> removeReservation(Long id) {
        return tempBookingServiceIntegration.removeReservation(id);
    }
}

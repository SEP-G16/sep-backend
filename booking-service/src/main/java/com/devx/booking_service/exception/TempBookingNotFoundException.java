package com.devx.booking_service.exception;

public class TempBookingNotFoundException extends RuntimeException {
    public TempBookingNotFoundException(String message) {
        super(message);
    }

    public TempBookingNotFoundException() {
    }

}

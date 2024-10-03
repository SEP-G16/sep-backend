package com.devx.booking_service.exception;

public class RoomTypeNotFoundException extends RuntimeException{
    public RoomTypeNotFoundException(String message) {
        super(message);
    }

    public RoomTypeNotFoundException() {
    }
}

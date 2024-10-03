package com.devx.table_reservation_service.exception;

public class ReservationNotFoundException extends RuntimeException{
    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(){}
}

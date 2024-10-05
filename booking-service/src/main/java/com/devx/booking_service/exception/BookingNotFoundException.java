package com.devx.booking_service.exception;

public class BookingNotFoundException extends RuntimeException{
    public BookingNotFoundException(){}

    public BookingNotFoundException(String message){
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

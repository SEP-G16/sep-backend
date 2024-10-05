package com.devx.booking_service.exception;

public class RoomTypeMismatchException extends RuntimeException{
    public RoomTypeMismatchException(){}

    public RoomTypeMismatchException(String message){
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

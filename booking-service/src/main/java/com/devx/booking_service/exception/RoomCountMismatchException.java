package com.devx.booking_service.exception;

public class RoomCountMismatchException extends RuntimeException{
    public RoomCountMismatchException(){}

    public RoomCountMismatchException(String message){
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

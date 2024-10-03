package com.devx.booking_service.exception;

public class RoomNotFoundException extends RuntimeException{
    public RoomNotFoundException(){}

    public RoomNotFoundException(String message){
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

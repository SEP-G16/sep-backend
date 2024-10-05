package com.devx.booking_service.exception;

public class NoRoomsSelectedException extends RuntimeException{

    public NoRoomsSelectedException(){}

    public NoRoomsSelectedException(String message){
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

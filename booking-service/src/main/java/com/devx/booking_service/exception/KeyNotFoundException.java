package com.devx.booking_service.exception;

public class KeyNotFoundException extends Exception{
    public KeyNotFoundException(){}

    public KeyNotFoundException(String message){
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}

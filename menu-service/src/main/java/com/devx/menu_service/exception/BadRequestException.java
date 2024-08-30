package com.devx.menu_service.exception;

public class BadRequestException extends RuntimeException{
    public BadRequestException(){}

    public BadRequestException(String message){super(message);}

    @Override
    public String toString() {
        return super.toString();
    }
}

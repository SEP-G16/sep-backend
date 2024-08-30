package com.devx.menu_service.exception;

public class AddOnNotFoundException extends RuntimeException{
    public AddOnNotFoundException(){}

    public AddOnNotFoundException(String message){super(message);}

    @Override
    public String toString() {
        return super.toString();
    }
}

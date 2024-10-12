package com.devx.menu_service.exception;

public class MenuItemNotFoundException extends RuntimeException{
    public MenuItemNotFoundException(){}

    public MenuItemNotFoundException(String message){super(message);}

    @Override
    public String toString() {
        return super.toString();
    }
}

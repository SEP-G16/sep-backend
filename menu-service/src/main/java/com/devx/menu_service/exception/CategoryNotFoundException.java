package com.devx.menu_service.exception;

public class CategoryNotFoundException extends RuntimeException{
    public CategoryNotFoundException(){}

    public CategoryNotFoundException(String message){super(message);}

    @Override
    public String toString() {
        return super.toString();
    }
}

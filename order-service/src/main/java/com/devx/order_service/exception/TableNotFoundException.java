package com.devx.order_service.exception;

public class TableNotFoundException extends RuntimeException{
    public TableNotFoundException(String message) {
        super(message);
    }

    public TableNotFoundException(){}
}

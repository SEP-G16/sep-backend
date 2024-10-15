package com.devx.order_service.exception;

public class OrderAlreadyCancelledException extends RuntimeException{
    public OrderAlreadyCancelledException(String message) {
        super(message);
    }

    public OrderAlreadyCancelledException(){}
}


package com.devx.order_service.exception;

public class OrderAlreadyCompletedException extends RuntimeException{
    public OrderAlreadyCompletedException(String message) {
        super(message);
    }

    public OrderAlreadyCompletedException(){}
}

package com.devx.order_service.exception;

public class OrderNotYetCompleteException extends RuntimeException{
    public OrderNotYetCompleteException(String message) {
        super(message);
    }

    public OrderNotYetCompleteException(){}
}

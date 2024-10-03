package com.devx.order_service.exception;

public class EmptyOrderItemListException extends RuntimeException {
    public EmptyOrderItemListException(String message) {
        super(message);
    }

    public EmptyOrderItemListException() {

    }
}

package com.devx.order_service.exception;

public class NullFieldException extends RuntimeException{
    public NullFieldException(String message) {
        super(message);
    }

    public NullFieldException(){}
}

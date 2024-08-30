package com.devx.booking_service.exception;

public class StoredProcedureCallException extends Exception{
    public StoredProcedureCallException(){}

    public StoredProcedureCallException(String message)
    {
        super(message);
    }
}

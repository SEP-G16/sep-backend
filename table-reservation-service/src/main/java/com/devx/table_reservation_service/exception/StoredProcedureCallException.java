package com.devx.table_reservation_service.exception;

public class StoredProcedureCallException extends RuntimeException{
    public StoredProcedureCallException(){}

    public StoredProcedureCallException(String message)
    {
        super(message);
    }
}

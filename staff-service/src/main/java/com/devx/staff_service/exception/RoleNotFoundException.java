package com.devx.staff_service.exception;

public class RoleNotFoundException extends RuntimeException{
    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(){}
}

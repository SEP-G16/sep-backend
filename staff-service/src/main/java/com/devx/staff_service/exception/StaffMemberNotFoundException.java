package com.devx.staff_service.exception;

public class StaffMemberNotFoundException extends RuntimeException{
    public StaffMemberNotFoundException(String message) {
        super(message);
    }

    public StaffMemberNotFoundException(){}
}

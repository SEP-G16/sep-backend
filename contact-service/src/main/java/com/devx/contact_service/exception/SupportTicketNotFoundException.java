package com.devx.contact_service.exception;

public class SupportTicketNotFoundException extends RuntimeException{
    public SupportTicketNotFoundException(String message) {
        super(message);
    }

    public SupportTicketNotFoundException(){}
}

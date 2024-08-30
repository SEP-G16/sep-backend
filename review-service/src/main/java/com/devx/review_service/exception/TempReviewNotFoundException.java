package com.devx.review_service.exception;

public class TempReviewNotFoundException extends RuntimeException{

    public TempReviewNotFoundException()
    {
        super();
    }

    public TempReviewNotFoundException(String message)
    {
        super(message);
    }
}

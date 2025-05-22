package com.example.order.Exception;

public class SessionTimeOutException extends RuntimeException {

    public SessionTimeOutException(String message)
    {
        super(message);
    }

}

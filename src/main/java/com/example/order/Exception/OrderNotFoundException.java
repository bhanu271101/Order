package com.example.order.Exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException(String message)
    {
        super(message);
    }

}

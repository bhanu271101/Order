package com.example.order.Exception;


public class CannotDeleteOrder extends RuntimeException{

    public CannotDeleteOrder(String message)
    {
        super(message);
    }

}

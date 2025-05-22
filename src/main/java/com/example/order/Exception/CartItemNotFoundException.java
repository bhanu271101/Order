package com.example.order.Exception;

public class CartItemNotFoundException extends RuntimeException{

    public CartItemNotFoundException(String message)
    {
        super(message);
    }

}

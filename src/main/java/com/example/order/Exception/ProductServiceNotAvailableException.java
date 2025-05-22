package com.example.order.Exception;


public class ProductServiceNotAvailableException extends RuntimeException {

    public ProductServiceNotAvailableException(String message)
    {
        super(message);
    }

}

package com.example.order.Exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {


    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFoundException(ProductNotFoundException productNotFoundException)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404),productNotFoundException.getMessage());
    }


    @ExceptionHandler(ProductServiceNotAvailableException.class)
    public ProblemDetail handleProductServiceNotAvailableException(ProductServiceNotAvailableException productServiceNotAvailableException)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500),productServiceNotAvailableException.getMessage());
    }


    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleOrderNotFoundException(OrderNotFoundException orderNotFoundException)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404),orderNotFoundException.getMessage());
    }
    
    @ExceptionHandler(CartItemNotFoundException.class)
    public ProblemDetail handleCartItemNotFoundException(CartItemNotFoundException cartItemNotFoundException)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(404),cartItemNotFoundException.getMessage());
    }


    @ExceptionHandler(CannotDeleteOrder.class)
    public ProblemDetail handleCannotDeleteOrder(CannotDeleteOrder cannotDeleteOrder)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(409),cannotDeleteOrder.getMessage());
    }

    @ExceptionHandler(SessionTimeOutException.class)
    public ProblemDetail handleSessionTimeOutException(SessionTimeOutException sessionTimeOutException)
    {
        return ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401),sessionTimeOutException.getMessage());
    }
}

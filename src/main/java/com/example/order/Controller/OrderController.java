package com.example.order.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.Dto.OrderDto;
import com.example.order.Service.OrderSerivce;
import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class OrderController {

    @Autowired
    private OrderSerivce orderSerivce;


    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable long id)
    {
        OrderDto orderDto=orderSerivce.getOrder(id);
        return new ResponseEntity<>(orderDto,HttpStatus.ACCEPTED);    
    }

    @PostMapping("/create")
    public ResponseEntity<String> createOrder(HttpServletRequest httpServletRequest,@RequestBody OrderDto orderDto) throws JsonProcessingException, MessagingException
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        return new ResponseEntity<>(orderSerivce.placeOrder(userId,httpServletRequest,orderDto),HttpStatus.CREATED);
    }

    
    @GetMapping("/getAllOrders")
    public ResponseEntity<List<OrderDto>> getAllOrderByUserId(HttpServletRequest httpServletRequest)
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        List<OrderDto> orderDtos=orderSerivce.getAllOrderByUserId(userId);
        return new ResponseEntity<>(orderDtos,HttpStatus.ACCEPTED);
    }

    @PostMapping("/buyFromCart")
    public String buyFromCart(HttpServletRequest httpServletRequest, @RequestParam("ids") String[] ids) throws MessagingException
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        List<Long> idList = Arrays.stream(ids)
            .map(Long::parseLong)  // Convert each String in the array to Long
            .collect(Collectors.toList());
        orderSerivce.buyFromCart(userId, httpServletRequest, idList);
        return "Order Placed Successfully";
    }

    @DeleteMapping("/deleteOrder")
    public void deleteOrder(@RequestParam("orderId") Long orderId)
    {
        orderSerivce.deleteOrder(orderId);
    }

}

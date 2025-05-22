package com.example.order.Utility;

import com.example.order.Dto.OrderDto;




public class OrderResponse {

    private String message;

    private int status;
    private OrderDto orderDto;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public OrderDto getOrderDto() {
		return orderDto;
	}
	public void setOrderDto(OrderDto orderDto) {
		this.orderDto = orderDto;
	}
    
    

}

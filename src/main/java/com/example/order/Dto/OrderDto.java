package com.example.order.Dto;
import java.time.LocalDateTime;

import com.example.order.Entity.AddressEntity;


public class OrderDto{
private String userId;

private String orderStatus;
private Long orderId;
private Long mobileId;
private String mobileName;
private Double price;
private int quantity;
private LocalDateTime createdAt;

public LocalDateTime getCreatedAt() {
	return createdAt;
}

public void setCreatedAt(LocalDateTime createdAt) {
	this.createdAt = createdAt;
}

private AddressEntity address;

public String getUserId() {
	return userId;
}

public void setUserId(String userId) {
	this.userId = userId;
}

public String getOrderStatus() {
	return orderStatus;
}

public void setOrderStatus(String orderStatus) {
	this.orderStatus = orderStatus;
}

public Long getOrderId() {
	return orderId;
}

public void setOrderId(Long orderId) {
	this.orderId = orderId;
}

public Long getMobileId() {
	return mobileId;
}

public void setMobileId(Long mobileId) {
	this.mobileId = mobileId;
}

public String getMobileName() {
	return mobileName;
}

public void setMobileName(String mobileName) {
	this.mobileName = mobileName;
}

public Double getPrice() {
	return price;
}

public void setPrice(Double price) {
	this.price = price;
}

public int getQuantity() {
	return quantity;
}

public void setQuantity(int quantity) {
	this.quantity = quantity;
}

public AddressEntity getAddress() {
	return address;
}

public void setAddress(AddressEntity address) {
	this.address = address;
}
}

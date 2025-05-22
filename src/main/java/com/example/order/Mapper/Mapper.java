package com.example.order.Mapper;

import org.springframework.stereotype.Service;

import com.example.order.Dto.CartDTO;
import com.example.order.Dto.OrderDto;
import com.example.order.Entity.CartEntity;
import com.example.order.Entity.OrderEntity;

@Service
public class Mapper {
    public OrderDto orderToOrderDTO(OrderEntity order) {
        if (order == null) return null;

        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setUserId(order.getUserId());
        dto.setMobileId(order.getMobileId());
        dto.setMobileName(order.getMobileName());
        dto.setPrice(order.getPrice());
        dto.setQuantity(order.getQuantity());
        dto.setAddress(order.getAddress());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setCreatedAt(order.getCreatedAt());
        return dto;
    }

    public OrderEntity orderDTOToOrder(OrderDto dto) {
        if (dto == null) return null;

        OrderEntity order = new OrderEntity();
        order.setOrderId(dto.getOrderId());
        order.setUserId(dto.getUserId());
        order.setMobileId(dto.getMobileId());
        order.setMobileName(dto.getMobileName());
        order.setPrice(dto.getPrice());
        order.setQuantity(dto.getQuantity());

        return order;
    }
    public CartEntity cartDtoToCartEntity(CartDTO cartDTO)
    {
        CartEntity cartEntity=new CartEntity();
        cartEntity.setMobileId(cartDTO.getMobileId());
        cartEntity.setAmount(cartDTO.getAmount());
        cartEntity.setQuantity(cartDTO.getQuantity());
        cartEntity.setUserId(cartDTO.getUserId());
        return cartEntity;
    }

    public CartDTO CartEntitytoCartDto(CartEntity cartEntity)
    {
        CartDTO cartDTO=new CartDTO();
        cartDTO.setAmount(cartEntity.getAmount());
        cartDTO.setId(cartEntity.getId());
        cartDTO.setMobileId(cartEntity.getMobileId());
        cartDTO.setQuantity(cartEntity.getQuantity());
        cartDTO.setUserId(cartEntity.getUserId());
        return cartDTO;
        
    }

    public OrderEntity cartEntityTOrderEntity(CartEntity cartEntity)
    {
        OrderEntity orderEntity=new OrderEntity();

        orderEntity.setMobileId(cartEntity.getMobileId());
        orderEntity.setQuantity(cartEntity.getQuantity());
        orderEntity.setUserId(cartEntity.getUserId());
        return orderEntity;
    }
}

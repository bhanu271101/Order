package com.example.order.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.example.order.Entity.OrderEntity;

import jakarta.transaction.Transactional;


public interface OrderRepository extends JpaRepository<OrderEntity,Long>
{
    
    List<OrderEntity> findByUserId(String userId);

    OrderEntity findByOrderId(long orderId);

    @Transactional
    @Modifying
    void deleteByOrderId(Long orderId);
}

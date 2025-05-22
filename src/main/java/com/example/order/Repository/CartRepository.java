package com.example.order.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.order.Entity.CartEntity;

import jakarta.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity,Long>{

        List<CartEntity> findByUserId(String userId);

        @Query("select sum(c.quantity) from CartEntity c where c.userId= :userId")
        Integer countByUserId(String userId);


        @Transactional
        @Modifying
        @Query("delete from CartEntity c where c.userId= :userId and c.id= :id")
        void deleteByUserIdAndId(String userId,Long id);
}

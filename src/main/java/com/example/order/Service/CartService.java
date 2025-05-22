package com.example.order.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.order.Dto.CartDTO;
import com.example.order.Dto.ProductDTO;
import com.example.order.Entity.CartEntity;
import com.example.order.Exception.ProductNotFoundException;
import com.example.order.Exception.SessionTimeOutException;
import com.example.order.Mapper.Mapper;
import com.example.order.Repository.CartRepository;

import jakarta.servlet.http.HttpServletRequest;


@Service
public class CartService {



    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private RestTemplate restTemplate;
    
    
    public Integer getCountOfItems(String userId)
    {
        return cartRepository.countByUserId(userId);
    }

    @CacheEvict(value = "cart",key="#userId",condition = "#userId != null")
    public String addToCart(String userId,CartDTO cartDTO)
    {
        cartDTO.setUserId(userId);
        if(userId==null)
        {
            throw new SessionTimeOutException("User session expired login to continue");
        }
        CartEntity cartEntity=mapper.cartDtoToCartEntity(cartDTO);
        Long mobileId=cartDTO.getMobileId();
        ResponseEntity<ProductDTO> response=restTemplate.getForEntity("http://localhost:8086/product/getProductById/{mobileId}",ProductDTO.class,mobileId);
        ProductDTO productDTO=response.getBody();
        if(productDTO.getQuantity()<cartDTO.getQuantity())
        {
            throw new ProductNotFoundException("Item out of stock");
        }
        cartDTO.setUserId(userId);
        int quantity=cartDTO.getQuantity();
        double price=quantity*productDTO.getPrice();
        cartEntity.setAmount(price);
        cartRepository.save(cartEntity);
        return "Items added to cart";

    }

    @Cacheable(value="cart",key="#userId", condition = "#userId != null")
    public List<CartDTO> getAllCartByUserId(String userId)
    {
        if(userId==null)
        {
            throw new SessionTimeOutException("User session expired login to continue");
        }
        System.out.println("fetching from DB");
        List<CartEntity> cartEntity=cartRepository.findByUserId(userId);
        List<CartDTO> list=new ArrayList<>();
        if(!cartEntity.isEmpty())
        {
            ListIterator<CartEntity> listIterator=cartEntity.listIterator();
            
            while(listIterator.hasNext())
            {
                list.add(mapper.CartEntitytoCartDto(listIterator.next()));
            }
            return list;
        }
        return list;
        
       
    }

    public CartEntity getCartById(Long id)
    {
        return cartRepository.getById(id);
    }

    @CacheEvict(value = "cart",key="#userId")
    public void deleteCartItemsByUserId(String userId,Long id)
    {
        if(userId==null)
        {
            throw new SessionTimeOutException("User session expired login to continue");
        }
        cartRepository.deleteByUserIdAndId(userId,id);
        
    }

    public void deleteCartItems(Long Id)
    {
        cartRepository.deleteById(Id);
    }

}

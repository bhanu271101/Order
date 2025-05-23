package com.example.order.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


import com.example.order.Dto.OrderDto;
import com.example.order.Dto.OrderStatusUpdateDTo;
import com.example.order.Dto.ProductDTO;
import com.example.order.Entity.AddressEntity;
import com.example.order.Entity.CartEntity;
import com.example.order.Entity.OrderEntity;
import com.example.order.Exception.CannotDeleteOrder;
import com.example.order.Exception.CartItemNotFoundException;
import com.example.order.Exception.OrderNotFoundException;
import com.example.order.Exception.ProductNotFoundException;
import com.example.order.Exception.ProductServiceNotAvailableException;
import com.example.order.Exception.SessionTimeOutException;
import com.example.order.Mapper.Mapper;
import com.example.order.Repository.OrderRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;




@org.springframework.stereotype.Service
public class OrderSerivce {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private Mapper mapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CartService cartService;


   

    @Autowired
    private RedisCacheManager redisCacheManager;

   

    public void cacheProxyForBuyFromCart(String userId,HttpServletRequest httpServletRequest,List<Long> ids)
    {
        this.buyFromCart(userId, httpServletRequest, ids);
    }

    private Long generateRandomOrderId() {
        Random random = new Random();
        long number = 1000000000L + (long) (random.nextDouble() * 9000000000L); // Ensures 10-digit number
        return number;
    }
  

    @SuppressWarnings("deprecation")
    public OrderDto getOrder(long orderId)
    {
        OrderEntity order=orderRepository.getById(orderId);
        return mapper.orderToOrderDTO(order);
    }

   

   

    @CacheEvict(value = "orders",key="#userId",condition = "#userId != null")
    public String placeOrder(String userId,HttpServletRequest httpServletRequest,OrderDto orderDto)
    {
        String authHeader=httpServletRequest.getHeader("Authorization");
        String token=authHeader.substring(7);
        if(userId==null)
        {
            throw new SessionTimeOutException("User session expired login to continue");
        }
        OrderEntity orderEntity=mapper.orderDTOToOrder(orderDto);
        Long orderId=generateRandomOrderId();
        Long mobileId=orderDto.getMobileId();
        
        
        try{
            ResponseEntity<ProductDTO> response=restTemplate.getForEntity("https://product-0gme.onrender.com/product/getProductById/{mobileId}",ProductDTO.class,mobileId);
            ProductDTO product=response.getBody();
            if(product.getQuantity()<orderDto.getQuantity())
            {
                throw new ProductNotFoundException("Item is out of stock");
            }
                
                ResponseEntity<AddressEntity> response1=restTemplate.getForEntity("https://mobileapp-4.onrender.com/getDefaultAddressForOrder/{token}",AddressEntity.class,token);
                AddressEntity addressEntity=response1.getBody();
                int quantity=orderDto.getQuantity();
                double price=quantity*product.getPrice();
               
                String mobileName=product.getMobileName();
                orderEntity.setMobileName(mobileName);
                orderEntity.setPrice(price);
                orderEntity.setOrderId(orderId);
                orderDto.setOrderId(orderId);
                orderDto.setUserId(userId);
                orderEntity.setUserId(userId);
                orderDto.setPrice(price);
                orderDto.setMobileName(mobileName);
                orderDto.setAddress(addressEntity);
                orderEntity.setAddress(addressEntity);
                orderEntity.setOrderStatus(orderDto.getOrderStatus());
                orderRepository.save(orderEntity);

                rabbitTemplate.convertAndSend("inventory-exchange","inventory.queue",orderDto);
                rabbitTemplate.convertAndSend("hub-exchange","hub.queue",orderDto);
                return "Order placed successfully";
        }
        catch(HttpClientErrorException.NotFound errorException)
        {
            throw new ProductNotFoundException("Product with ID "+mobileId+" not found");
        }
        catch(HttpServerErrorException errorException)
        {
             throw new RuntimeException("Product service returned an internal error");
        }
        catch(ResourceAccessException exception)
        {
            throw new ProductServiceNotAvailableException("Product service is unavalible");
        }
       
        
       
            
        
    }


    @Cacheable(value = "orders", key = "#userId",condition = "#userId != null",unless = "#result == null")
    public List<OrderDto> getAllOrderByUserId(String userId)
    {
        System.out.println("printing from BD");
        if(userId==null)
        {
            throw new SessionTimeOutException("User session expired login to continue");
        }
        List<OrderEntity> orderEntity=orderRepository.findByUserId(userId);
        List<OrderDto> orderDtoList=new ArrayList<>();
        if(orderEntity.isEmpty())
        {
            throw new OrderNotFoundException("No orders found for this customer");
        }
        ListIterator<OrderEntity> listIterator=orderEntity.listIterator();
        while(listIterator.hasNext())
        {
            orderDtoList.add(mapper.orderToOrderDTO(listIterator.next()));
        }

        return orderDtoList;

    }

    

    @Transactional
    @RabbitListener(queues = "order.queue")
    public void orderStatusUpdate(OrderStatusUpdateDTo orderStatusUpdateDTo)
    {
            OrderEntity orderEntity=orderRepository.findByOrderId(orderStatusUpdateDTo.getOrderId());
            if(orderEntity != null)
            {
                orderEntity.setOrderStatus(orderStatusUpdateDTo.getOrderStatus());
                orderRepository.save(orderEntity);
                redisCacheManager.getCache("orders").evict(orderEntity.getUserId());
            }
            return;
            
    }


    @Transactional
    public void deleteOrder(Long orderId)
    {
        OrderEntity orderEntity=orderRepository.findByOrderId(orderId);
        if(orderEntity.getOrderStatus().equals("Delivered"))
        {
            throw new CannotDeleteOrder("Can't delete delivered order");
        }
        redisCacheManager.getCache("orders").evict(orderEntity.getUserId());
        orderEntity.setOrderStatus("Cancelled");
        orderRepository.save(orderEntity);
        rabbitTemplate.convertAndSend("hub-exchange","hub.delete.queue", orderId);
    }

    @CacheEvict(value={"cart","orders"}, key="#userId")
    public void buyFromCart(String userId,HttpServletRequest httpServletRequest,List<Long> cartIds)
    {
        if(userId==null)
        {
            throw new SessionTimeOutException("User session expired login to continue");
        }
        String authHeader=httpServletRequest.getHeader("Authorization");
        String token=authHeader.substring(7);
        ResponseEntity<AddressEntity> response1=restTemplate.getForEntity("https://mobileapp-4.onrender.com/getDefaultAddressForOrder/{token}",AddressEntity.class,token);
        AddressEntity addressEntity=response1.getBody();
        List<OrderEntity> orderEntities=new ArrayList<>();
        for(Long cartId:cartIds)
        {
            System.out.println(cartId);
            CartEntity cartEntity=cartService.getCartById(cartId);
            if(cartEntity==null)
            {
                throw new CartItemNotFoundException("CartItem with Id"+cartId+"is not present");
            }
            OrderEntity orderEntity=mapper.cartEntityTOrderEntity(cartEntity);
            
            if(addressEntity!=null)
            {
                Long mobileId=cartEntity.getMobileId();
                try
                {
                ResponseEntity<ProductDTO> response=restTemplate.getForEntity("https://product-0gme.onrender.com/product/getProductById/{mobileId}",ProductDTO.class,mobileId);
                ProductDTO product=response.getBody();
                
                if(product.getQuantity()<cartEntity.getQuantity())
                {
                    throw new ProductNotFoundException("Item is out of stock");
                }
                     int quantity=cartEntity.getQuantity();
                    double price=quantity*product.getPrice();
                    String mobileName=product.getMobileName();
                    orderEntity.setMobileName(mobileName);
                    orderEntity.setPrice(price);
                    orderEntity.setOrderId(generateRandomOrderId());
                    orderEntity.setAddress(addressEntity);
                    orderEntity.setUserId(userId);
                    orderEntity.setOrderStatus("Order placed");
                    OrderDto orderDto=mapper.orderToOrderDTO(orderEntity);
                    orderDto.setAddress(addressEntity);
                    orderDto.setOrderStatus("Order placed");
                    rabbitTemplate.convertAndSend("inventory-exchange","inventory.queue",orderDto);
                    rabbitTemplate.convertAndSend("hub-exchange","hub.queue",orderDto);
                    orderEntities.add(orderEntity);
                    cartService.deleteCartItems(cartId);
            }
            catch(HttpServerErrorException httpServerErrorException)
            {
                throw new RuntimeException("Product service is throwing internal server error");
            }
            catch(ResourceAccessException resourceAccessException)
            {
                throw new ProductServiceNotAvailableException("Product service is not available");
            }
        }

        }
        orderRepository.saveAll(orderEntities);
    }

}

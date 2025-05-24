package com.example.order.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.order.Authorization.JwtToken;
import com.example.order.Dto.CartDTO;
import com.example.order.Service.CartService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class CartController {


    @Autowired
    private CartService cartService;

    @Autowired
    private JwtToken jwtToken;

    @GetMapping("/getCount")
    public Integer getCountOfItems(HttpServletRequest httpServletRequest)
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        return cartService.getCountOfItems(userId);
    }

     @GetMapping("/cronjob")
    public String dummyForCronjob()
    {
        return "Cronjob ran successfully";
    }

    @PostMapping("/addToCart")
    public String addToCart(HttpServletRequest httpServletRequest,@RequestBody CartDTO cartDTO)
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        return cartService.addToCart(userId,cartDTO);
    }

    
    @GetMapping("/getAllCart")
    public List<CartDTO> getAllCartByUserId(HttpServletRequest httpServletRequest)
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        return cartService.getAllCartByUserId(userId);
    }

    @DeleteMapping("/deleteCartById")
    public void deleteItemFromCart(HttpServletRequest httpServletRequest,@RequestParam("cartId") Long id)
    {
        String userId=(String)httpServletRequest.getAttribute("userId");
        cartService.deleteCartItemsByUserId(userId,id);
    }

}

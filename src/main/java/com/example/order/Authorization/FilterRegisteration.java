package com.example.order.Authorization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterRegisteration {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public FilterRegistrationBean<JwtRequestFilter> filterRegistrationBean()
    {
        FilterRegistrationBean<JwtRequestFilter> filterRegistrationBean=new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(jwtRequestFilter);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }
}

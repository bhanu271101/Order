package com.example.order.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration
public class RabbitMQConfig {

   
    @Bean
    public Queue orderQueue()
    {
        return new Queue("order.queue");
    }

    @Bean
    public DirectExchange directExchange()
    {
        return new DirectExchange("order-exchange");
    }

    @Bean
    public Binding binding(@Qualifier("orderQueue") Queue queue,DirectExchange directExchange)
    {
        return BindingBuilder.bind(queue).to(directExchange).with("order.queue");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory)
    {
        RabbitTemplate template=new RabbitTemplate(connectionFactory);
        template.setReplyTimeout(10000);
        template.setMessageConverter(messageConverter());
        return template;
    }

}

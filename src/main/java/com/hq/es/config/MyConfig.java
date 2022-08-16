package com.hq.es.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;

//@Configuration
public class MyConfig {

    //@Bean
    public Queue directQueue(){
        return new Queue("test01",false);
    }
   // @Bean
    public DirectExchange exchange(){
        return new DirectExchange("testExchange",false,false);
    }
    //Bean
    public Binding binds() {
        return  BindingBuilder.bind(directQueue()).to(exchange()).withQueueName();
    }

}

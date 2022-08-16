package com.hq.es.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "test01")
public class consumer91 {

    @RabbitHandler
    public void handle(String o){
        System.out.println(o.toString());
    }
}

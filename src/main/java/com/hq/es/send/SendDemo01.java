package com.hq.es.send;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendDemo01 {
    @Autowired
    RabbitTemplate rabbitTemplate;

    @RequestMapping("/send")
    @ResponseBody
    public void send (){
       // rabbitTemplate.convertAndSend("testExchange","test01","你好！");
     //   rabbitTemplate.convertAndSend("你好！");
    }}
package com.hq.es.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CustomConfig {
    @Bean
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }

}

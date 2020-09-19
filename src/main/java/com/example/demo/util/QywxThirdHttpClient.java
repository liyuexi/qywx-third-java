package com.example.demo.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class QywxThirdHttpClient {

    @Bean
    public RestTemplate QywxThirdHttpClient(){
        return new RestTemplate();
    }

}

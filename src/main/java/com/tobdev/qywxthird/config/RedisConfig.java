package com.tobdev.qywxthird.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {
//    @Bean
//    public RedisTemplate<String,String> redisTemplate(RedisConnectionFactory factory){
//        RedisTemplate<String ,String > redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(factory);
//        return  redisTemplate;
//    }
        @Bean
        StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
            return new StringRedisTemplate(connectionFactory);
        }


}

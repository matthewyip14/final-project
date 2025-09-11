package com.bootcamp.project_stock_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.web.client.RestTemplate;

import com.bootcamp.project_stock_data.model.dto.CompanyDTO;

@Configuration
public class RedisConfig {

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory("localhost", 6379); // 確保與實際 Redis 地址匹配
  }

  @Bean
  public RedisTemplate<String, CompanyDTO> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, CompanyDTO> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setDefaultSerializer(new Jackson2JsonRedisSerializer<>(CompanyDTO.class));
    return template;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}

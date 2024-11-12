package com.webrtc.signaling.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.webrtc.signaling.model.dto.ChatMessageDTO;

import lombok.NoArgsConstructor;

@NoArgsConstructor
@Configuration
@EnableRedisHttpSession()
public class RedisConfig {
   @Value("${spring.data.redis.host}")
   private String redisHost;
   
   @Value("${spring.data.redis.port}")
   private int redisPort;
   
//   @Value("${spring.data.redis.password}")
//   private String redisPassword;
   
   @Bean 
   RedisConnectionFactory redisConnectionFactory() {
      RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
      redisStandaloneConfiguration.setHostName(redisHost);
      redisStandaloneConfiguration.setPort(redisPort);
//      redisStandaloneConfiguration.setPassword(redisPassword);
      return new LettuceConnectionFactory(redisStandaloneConfiguration);
   }
   //16진수형태의 세션값을 읽을수 있는 문자열로 변경
   @Bean
   RedisSerializer<Object> springSessionDefaultRedisSerializer() {
      return new GenericJackson2JsonRedisSerializer();
   }
   
   @Bean 
   StringRedisTemplate stringRedisTemplate() {
      StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
      //redis 연결객체 설정  
      stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
      
      //key 저장 방법 설정
      stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
      
      //value 저장 방법 설정
      stringRedisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
      
      //default값 저장 방법 설정
      stringRedisTemplate.setDefaultSerializer(new StringRedisSerializer());
      
      return stringRedisTemplate;
   }
   
   @Bean
   RedisTemplate<String, ChatMessageDTO> redisTemplate(RedisConnectionFactory connectionFactory) {
       // ChatMessageDTO를 위한 직렬화 설정
       RedisTemplate<String, ChatMessageDTO> template = new RedisTemplate<>();
       template.setConnectionFactory(connectionFactory);
       
       template.setKeySerializer(new StringRedisSerializer());
       template.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDTO.class));
       
       template.setHashKeySerializer(new StringRedisSerializer());
       template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDTO.class));
       
       template.setEnableTransactionSupport(true);
       template.afterPropertiesSet();
       
       return template;
   }
}

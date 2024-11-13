package com.ssafy.enjoycamping.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	
	@Bean
	public RedisConnectionFactory redisConnectionFactory() { //RedisConnectionFactory는 Redis서버와의 연결을 담당하는 객체
		return new LettuceConnectionFactory();
	}
	
    @Bean
    public RedisTemplate<String, String> redisTemplate() { //RedisTemplate은 Redis에서 데이터를 읽고 쓰는 작업을 담당
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory()); //Redis 서버와 연결을 설정
        //Redis는 데이터를 바이너리 형식으로 저장하므로, 키와 값이 문자열일 경우 이를 직렬화해야한다.
        //StringRedisSerializer를 사용하면 Redis에 저장되는 키가 UTF-8 인코딩된 문자열로 직렬화 및 역직렬화를 처리한다.
        redisTemplate.setKeySerializer(new StringRedisSerializer()); 
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
	
}

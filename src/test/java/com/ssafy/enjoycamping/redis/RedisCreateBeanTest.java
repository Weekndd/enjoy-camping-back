package com.ssafy.enjoycamping.redis;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisCreateBeanTest {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	@DisplayName("레디스 생성 테스트")
	void RedisCreateBeanTest() { 
		redisTemplate.opsForValue().set("testKey", "testValue");
		String value = redisTemplate.opsForValue().get("testKey");
		assertNotNull(redisTemplate);
		assertTrue(value.equals("testValue"));
		System.out.println(value);
	}
	
}

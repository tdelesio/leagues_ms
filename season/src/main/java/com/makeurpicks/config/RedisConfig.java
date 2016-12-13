package com.makeurpicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	
	
//		@Bean
//	    public RedisConnectionFactory redisConnectionFactory() {
//	        return new JedisConnectionFactory();
//	    }
	
	@Bean
	public StringRedisSerializer stringRedisSerializer()
	{
		return new StringRedisSerializer();
	}
	
	

	
	
	
}

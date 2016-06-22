package com.makeurpicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.makeurpicks.domain.Season;
import com.makeurpicks.repository.SeasonRepository;
import com.makeurpicks.repository.redis.RedisSeasonRepository;

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

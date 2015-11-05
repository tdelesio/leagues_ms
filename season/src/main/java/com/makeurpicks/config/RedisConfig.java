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

	@Bean
	public RedisTemplate<String, Season> seasonRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Season> template = new RedisTemplate<String, Season>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setHashKeySerializer(stringRedisSerializer());
		template.setKeySerializer(stringRedisSerializer());
		template.setHashValueSerializer(userJsonRedisSerializer());
//		template.setDefaultSerializer(new StringRedisSerializer());
		return template;
	}
	
//		@Bean
//	    public RedisConnectionFactory redisConnectionFactory() {
//	        return new JedisConnectionFactory();
//	    }
	
	@Bean
	public StringRedisSerializer stringRedisSerializer()
	{
		return new StringRedisSerializer();
	}
	
	@Bean
	public Jackson2JsonRedisSerializer<Season> userJsonRedisSerializer()
	{
		return new Jackson2JsonRedisSerializer<>(Season.class);
	}
	
	@Bean
	public SeasonRepository seasonRespository(RedisConnectionFactory redisConnectionFactory)
	{
		return new RedisSeasonRepository(seasonRedisTemplate(redisConnectionFactory));
	}

	
	
	
}

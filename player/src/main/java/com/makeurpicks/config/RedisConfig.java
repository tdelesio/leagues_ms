package com.makeurpicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Player;
import com.makeurpicks.repository.PlayerRepository;
import com.makeurpicks.repository.redis.RedisPlayerByUsernameRepository;
import com.makeurpicks.repository.redis.RedisPlayerRepository;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Player> playerRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Player> template = new RedisTemplate<String, Player>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	
	
	@Bean
	public PlayerRepository playerRepository(RedisConnectionFactory redisConnectionFactory)
	{
		PlayerRepository playerRepository = new RedisPlayerRepository(playerRedisTemplate(redisConnectionFactory));
		return playerRepository;
	}
	
	@Bean
	public RedisPlayerByUsernameRepository redisPlayerByUsernameRepository(RedisConnectionFactory redisConnectionFactory)
	{
		return new RedisPlayerByUsernameRepository(playerRedisTemplate(redisConnectionFactory));
	}
	
	
}

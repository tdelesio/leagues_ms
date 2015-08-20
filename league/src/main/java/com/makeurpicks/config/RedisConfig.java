package com.makeurpicks.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.KeyValue;
import com.makeurpicks.domain.League;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.redis.RedisLeagueRepository;
import com.makeurpicks.repository.redis.RedisLeaguesPlayerHasJoinedRepository;
import com.makeurpicks.repository.redis.RedisPlayersInLeagueRespository;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, League> leagueRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, League> template = new RedisTemplate<String, League>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, KeyValue> keyValueRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, KeyValue> template = new RedisTemplate<String, KeyValue>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public LeagueRepository leagueRepository(RedisConnectionFactory redisConnectionFactory)
	{
		LeagueRepository leagueRepository = new RedisLeagueRepository(leagueRedisTemplate(redisConnectionFactory));
		return leagueRepository;
	}
	
	@Bean
	public RedisPlayersInLeagueRespository playersInLeagueRespository(RedisConnectionFactory redisConnectionFactory)
	{
		return new RedisPlayersInLeagueRespository(keyValueRedisTemplate(redisConnectionFactory));
	}
	
	@Bean
	public RedisLeaguesPlayerHasJoinedRepository leaguesPlayerHasJoinedRepository(RedisConnectionFactory redisConnectionFactory)
	{	
		return new RedisLeaguesPlayerHasJoinedRepository(keyValueRedisTemplate(redisConnectionFactory));
	}
}

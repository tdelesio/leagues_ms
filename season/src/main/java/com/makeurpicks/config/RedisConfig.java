package com.makeurpicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

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
		
		return template;
	}
	
//	@Bean
//	public RedisTemplate<String, Team> teamRedisTemplate(
//			RedisConnectionFactory redisConnectionFactory) {
//		RedisTemplate<String, Team> template = new RedisTemplate<String, Team>();
//		template.setConnectionFactory(redisConnectionFactory);
//		
//		return template;
//	}
	
	
	
//	@Bean
//	public TeamRepository leagueRepository(RedisConnectionFactory redisConnectionFactory)
//	{
//		TeamRepository teamRepository = new RedisTeamRepository(teamRedisTemplate(redisConnectionFactory));
//		return teamRepository;
//	}
	
	@Bean
	public SeasonRepository seasonRespository(RedisConnectionFactory redisConnectionFactory)
	{
		return new RedisSeasonRepository(seasonRedisTemplate(redisConnectionFactory));
	}

	
	
	
}

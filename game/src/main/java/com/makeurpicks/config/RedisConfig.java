package com.makeurpicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.Team;
import com.makeurpicks.domain.Week;
import com.makeurpicks.repository.GameRepository;
import com.makeurpicks.repository.SeasonRepository;
import com.makeurpicks.repository.TeamRepository;
import com.makeurpicks.repository.WeekRepository;
import com.makeurpicks.repository.redis.RedisGameRepository;
import com.makeurpicks.repository.redis.RedisSeasonRepository;
import com.makeurpicks.repository.redis.RedisTeamRepository;
import com.makeurpicks.repository.redis.RedisWeekRepository;

@Configuration
public class RedisConfig {

	
	@Bean
	public RedisTemplate<String, Game> gameRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Game> template = new RedisTemplate<String, Game>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, Team> teamRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Team> template = new RedisTemplate<String, Team>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, Season> seasonRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Season> template = new RedisTemplate<String, Season>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, Week> weekRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Week> template = new RedisTemplate<String, Week>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	
	
	
	@Bean
	public SeasonRepository seasonRepository(RedisConnectionFactory redisConnectionFactory)
	{
		SeasonRepository seasonRepository = new RedisSeasonRepository(seasonRedisTemplate(redisConnectionFactory));
		return seasonRepository;
	}
	
	@Bean
	public WeekRepository weekRepository(RedisConnectionFactory redisConnectionFactory)
	{
		WeekRepository weekRepository = new RedisWeekRepository(weekRedisTemplate(redisConnectionFactory));
		return weekRepository;
	}
	
	@Bean
	public GameRepository gameRepository(RedisConnectionFactory redisConnectionFactory)
	{
		GameRepository gameRepository = new RedisGameRepository(gameRedisTemplate(redisConnectionFactory));
		return gameRepository;
	}
	
	@Bean
	public TeamRepository teamRepository(RedisConnectionFactory redisConnectionFactory)
	{
		TeamRepository teamRepository = new RedisTeamRepository(teamRedisTemplate(redisConnectionFactory));
		return teamRepository;
	}
	
	
}

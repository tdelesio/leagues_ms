package com.makeurpicks.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeaguesPlayerJoined;
import com.makeurpicks.domain.PlayersInLeague;
import com.makeurpicks.domain.Season;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.SeasonRepository;
import com.makeurpicks.repository.redis.RedisLeagueRepository;
import com.makeurpicks.repository.redis.RedisLeaguesPlayerHasJoinedRepository;
import com.makeurpicks.repository.redis.RedisPlayersInLeagueRespository;
import com.makeurpicks.repository.redis.RedisSeasonRepository;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, League> leagueRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, League> template = new RedisTemplate<String, League>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setHashKeySerializer(stringRedisSerializer());
		template.setKeySerializer(stringRedisSerializer());
		template.setHashValueSerializer(leagueJsonRedisSerializer());
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, LeaguesPlayerJoined> leaguesPlayerJoinedRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, LeaguesPlayerJoined> template = new RedisTemplate<String, LeaguesPlayerJoined>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setHashKeySerializer(stringRedisSerializer());
		template.setKeySerializer(stringRedisSerializer());
		template.setHashValueSerializer(leaguePlayerJoinedJsonRedisSerializer());
		return template;
	}
	
	@Bean
	public RedisTemplate<String, PlayersInLeague> playersInLeagueRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, PlayersInLeague> template = new RedisTemplate<String, PlayersInLeague>();
		template.setConnectionFactory(redisConnectionFactory);
		template.setHashKeySerializer(stringRedisSerializer());
		template.setKeySerializer(stringRedisSerializer());
		template.setHashValueSerializer(playersInLeagueJsonRedisSerializer());
		
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
		return new RedisPlayersInLeagueRespository(playersInLeagueRedisTemplate(redisConnectionFactory));
	}
	
	@Bean
	public RedisLeaguesPlayerHasJoinedRepository leaguesPlayerHasJoinedRepository(RedisConnectionFactory redisConnectionFactory)
	{	
		return new RedisLeaguesPlayerHasJoinedRepository(leaguesPlayerJoinedRedisTemplate(redisConnectionFactory));
	}
	
	@Bean
	public StringRedisSerializer stringRedisSerializer()
	{
		return new StringRedisSerializer();
	}
	
	@Bean
	public Jackson2JsonRedisSerializer<League> leagueJsonRedisSerializer()
	{
		return new Jackson2JsonRedisSerializer<>(League.class);
	}
	
	@Bean
	public Jackson2JsonRedisSerializer<LeaguesPlayerJoined> leaguePlayerJoinedJsonRedisSerializer()
	{
		return new Jackson2JsonRedisSerializer<>(LeaguesPlayerJoined.class);
	}
	
	@Bean
	public Jackson2JsonRedisSerializer<PlayersInLeague> playersInLeagueJsonRedisSerializer()
	{
		return new Jackson2JsonRedisSerializer<>(PlayersInLeague.class);
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
	
	
}

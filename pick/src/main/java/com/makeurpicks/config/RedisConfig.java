package com.makeurpicks.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.repository.DoublePickRepository;
import com.makeurpicks.repository.PickRepository;
import com.makeurpicks.repository.PicksByWeekRepository;
import com.makeurpicks.repository.redis.RedisDoublePlckRepository;
import com.makeurpicks.repository.redis.RedisPickRepository;
import com.makeurpicks.repository.redis.RedisPicksByWeekRepository;

@Configuration
public class RedisConfig {

	@Bean
	public RedisTemplate<String, Pick> pickRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Pick> template = new RedisTemplate<String, Pick>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}  
	
	@Bean
	public RedisTemplate<String, DoublePick> doublePickRedisTemplate(
			RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, DoublePick> template = new RedisTemplate<String, DoublePick>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, String> stringRedisTemplte(RedisConnectionFactory redisConnectionFactory)
	{
		RedisTemplate<String, String> template = new RedisTemplate<String, String>();
		template.setConnectionFactory(redisConnectionFactory);
		
		return template;
	}
	
	@Bean
	public RedisTemplate<String, Map<String, Map<String, String>>> picksByWeekRedisTemplate(RedisConnectionFactory redisConnectionFactory)
	{
		RedisTemplate<String, Map<String, Map<String, String>>> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
	
	
	
	@Bean
	public PickRepository pickRepository(RedisConnectionFactory redisConnectionFactory)
	{
		PickRepository pickRepository = new RedisPickRepository(pickRedisTemplate(redisConnectionFactory));
		return pickRepository;
	}
	
	@Bean
	public DoublePickRepository doublePickRepository(RedisConnectionFactory redisConnectionFactory)
	{
		DoublePickRepository doublePickRepository = new RedisDoublePlckRepository(doublePickRedisTemplate(redisConnectionFactory));
		return doublePickRepository;
	}
	
	@Bean
	public PicksByWeekRepository picksByWeekRepository(RedisConnectionFactory redisConnectionFactory)
	{
		PicksByWeekRepository picksByWeekRepository = new RedisPicksByWeekRepository(picksByWeekRedisTemplate(redisConnectionFactory));
		return picksByWeekRepository;
	}
	
//	@Bean 
//	public RedisPicksByLeagueWeekRepository redisPicksByLeagueWeekRepository(RedisConnectionFactory redisConnectionFactory)
//	{
//		return new RedisPicksByLeagueWeekRepository(stringRedisTemplte(redisConnectionFactory));
//	}
//	
//	@Bean 
//	public RedisPicksByLeagueWeekAndPlayerRepository redisPicksByLeagueWeekRepositoryAndPlayer(RedisConnectionFactory redisConnectionFactory)
//	{
//		return new RedisPicksByLeagueWeekAndPlayerRepository(stringRedisTemplte(redisConnectionFactory));
//	}
}

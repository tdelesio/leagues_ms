package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.League;
import com.makeurpicks.repository.LeagueRepository;

public class RedisLeagueRepository extends AbstractRedisCRUDRepository<League> implements LeagueRepository {

	
	
	public static final String LEAGUES_KEY = "leagues";
	
	public RedisLeagueRepository(RedisTemplate<String, League> redisTemplate)
	{
		super(redisTemplate);
	}

	@Override
	public String getKey() {
		return LEAGUES_KEY;
	}
	
	

}

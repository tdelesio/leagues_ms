package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Player;
import com.makeurpicks.repository.PlayerRepository;

public class RedisPlayerRepository extends AbstractRedisHashCRUDRepository<Player> implements PlayerRepository {

public static final String PLAYER_KEY = "players_by_username";
	
	public RedisPlayerRepository(RedisTemplate<String, Player> redisTemplate)
	{
		super(redisTemplate);
	}

	@Override
	public String getKey() {
		return PLAYER_KEY;
	}	

}

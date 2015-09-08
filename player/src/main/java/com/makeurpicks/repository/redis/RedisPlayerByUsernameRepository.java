package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Player;
import com.makeurpicks.repository.PlayerByUsernameRepository;
import com.makeurpicks.repository.PlayerRepository;

public class RedisPlayerByUsernameRepository extends AbstractRedisHashCRUDRepository<Player> implements PlayerByUsernameRepository {

public static final String PLAYER_KEY = "players";
	
	public RedisPlayerByUsernameRepository(RedisTemplate<String, Player> redisTemplate)
	{
		super(redisTemplate);
	}

	@Override
	public String getKey() {
		return PLAYER_KEY;
	}	

	public void delete(Player p) {
		hashOps.delete(getKey(), p.getUsername());
	}
	
	public Player save(Player t) {
		hashOps.put(getKey(), t.getUsername(), t);
		return t;
	}
	
	
}

package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.repository.DoublePickRepository;

public class RedisDoublePlckRepository extends AbstractRedisHashCRUDRepository<DoublePick> implements DoublePickRepository {

	public RedisDoublePlckRepository(RedisTemplate<String, DoublePick> redisTemplate)
	{
		super(redisTemplate);
	}

	@Override
	public String getKey() {
		return "double_pick";
	}
	
	
}


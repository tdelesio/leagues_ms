package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Week;
import com.makeurpicks.repository.WeekRepository;

public class RedisWeekRepository extends AbstractRedisSetCRUDRepository<Week>
		implements WeekRepository {

	public RedisWeekRepository(RedisTemplate<String, Week> redisTemplate)
	{
		super(redisTemplate);
	}
	
	public final static String key_prefix = "WEEKS_BY_SEASON-";
	
	private String buildKey(String seasonId)
	{
		return key_prefix+seasonId;
	}
	
	public Week createUpdateWeek(Week week)
	{
		add(buildKey(week.getSeasonId()), week);
		return week;
	}
	
	public Iterable<Week> getWeeksBySeason(String seasonId)
	{
		return findAll(buildKey(seasonId));
	}
	

	
}

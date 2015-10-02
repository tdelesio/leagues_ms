package com.makeurpicks.repository.redis;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Pick;
import com.makeurpicks.repository.PicksByLeagueWeekRepository;

public class RedisPicksByLeagueWeekRepository extends AbstractRedisSetCRUDRepository implements PicksByLeagueWeekRepository{

	public RedisPicksByLeagueWeekRepository(RedisTemplate<String, String> redisTemplate)
	{
		super(redisTemplate);
	}
	
	

	public String buildKey(String seasonId, String weekId) {
		StringBuilder build = new StringBuilder("picks_by_league_week+");
		build.append(seasonId);
		build.append("+");
		build.append(weekId);
		return build.toString();
	}

	


	public void addPick(Pick pick)
	{
		add(buildKey(pick.getLeagueId(),pick.getWeekId()),pick.getId());
	}
	
	public Iterable<String> getPicksForLeagueAndWeek(String leagueId, String weekId)
	{
		return findAll(buildKey(leagueId, weekId));
	}
}

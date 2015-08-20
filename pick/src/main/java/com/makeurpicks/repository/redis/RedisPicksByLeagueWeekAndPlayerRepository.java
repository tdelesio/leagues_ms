package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Pick;
import com.makeurpicks.repository.PicksByLeagueWeekAndPlayerRepository;

public class RedisPicksByLeagueWeekAndPlayerRepository extends AbstractRedisSetCRUDRepository implements PicksByLeagueWeekAndPlayerRepository{

	public RedisPicksByLeagueWeekAndPlayerRepository(RedisTemplate<String, String> redisTemplate)
	{
		super(redisTemplate);
	}
	
	

	public String buildKey(String leagueId, String weekId, String playerId) {
		StringBuilder build = new StringBuilder("picks_by_league_week+");
		build.append(leagueId);
		build.append("+");
		build.append(weekId);
		build.append("+");
		build.append(playerId);
		return build.toString();
	}

	


	public void addPick(Pick pick)
	{
		add(buildKey(pick.getLeagueId(),pick.getWeekId(), pick.getPlayerId()),pick.getId());
	}
	
	public Iterable<String> getPicksForLeagueWeekAndPlayer(String leagueId, String weekId, String playerId)
	{
		return findAll(buildKey(leagueId, weekId, playerId));
	}


	
	
}

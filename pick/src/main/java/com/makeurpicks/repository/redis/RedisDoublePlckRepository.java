package com.makeurpicks.repository.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.repository.DoublePickRepository;

import scala.remote;

public class RedisDoublePlckRepository implements DoublePickRepository {

	protected final HashOperations<String, String, Map<String, DoublePick>> hashOps;
	
	public RedisDoublePlckRepository(RedisTemplate<String, Map<String, DoublePick>> redisTemplate)
	{
		this.hashOps = redisTemplate.opsForHash();
	}

//	@Override
	public String getKey() {
		return "double_pick";
	}
	
	private String buildKey(String leagueId, String weekId)
	{
		return new StringBuilder(leagueId).append("+").append(weekId).toString();
	}
	
	public Map<String, DoublePick> findAllForLeagueAndWeek(String leagueId, String weekId)
	{
		String hashkey = buildKey(leagueId, weekId);
		return findAllForLeagueAndWeek(hashkey);
	}
	
	public Map<String, DoublePick> findAllForLeagueAndWeek(String hashkey)
	{
		Map<String, DoublePick> doublePicks = hashOps.get(getKey(), hashkey);
		return doublePicks;
	}
	
	public DoublePick findDoubleForPlayer(String leagueId, String weekId, String playerId)
	{
		Map<String, DoublePick> doublePicks = findAllForLeagueAndWeek(leagueId, weekId);
		if (doublePicks==null || doublePicks.isEmpty())
			return null;
		
		return doublePicks.get(playerId);
		
	}
	
	public void deleteAll()
	{
		for (String key:hashOps.keys(getKey()))
		{
			hashOps.delete(getKey(), key);
		}
	}
	
	public void delete(DoublePick doublePick)
	{
		Map<String, DoublePick> doublePicks = findAllForLeagueAndWeek(doublePick.getId());
		doublePicks.remove(doublePick.getPlayerId());
	}
	
	public void save(DoublePick doublePick)
	{
		Map<String, DoublePick> doublePicks = findAllForLeagueAndWeek(doublePick.getId());
		if (doublePicks == null)
			doublePicks = new HashMap<>();
		
		doublePicks.put(doublePick.getPlayerId(), doublePick);
		
		hashOps.put(getKey(), doublePick.getId(), doublePicks);
	}
}


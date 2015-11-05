package com.makeurpicks.repository.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Pick;
import com.makeurpicks.repository.PicksByWeekRepository;

public class RedisPicksByWeekRepository implements PicksByWeekRepository {

	
	protected final HashOperations<String, String, Map<String, Map<String, String>>> hashOps;
	
	public RedisPicksByWeekRepository(RedisTemplate<String, Map<String, Map<String, String>>> redisTemplate) {
		this.hashOps = redisTemplate.opsForHash();
	}
	
	private static final String key = "PICKS_BY_WEEK";
	
	public void deleteAll()
	{
		Set<String> ids = hashOps.keys(key);
		for (String id : ids) {
			hashOps.delete(key, id);
		}
	}
	
	//return a list of players
	public Map<String, Map<String, String>> getPlayersByWeek(String weekId)
	{
		 Map<String, Map<String, String>> playerMap = hashOps.get(key, weekId);
		 return playerMap;
	}
	
	public Map<String, String> getGamesByPlayer(String weekId, String playerId)
	{
		Map<String, String> gameMap = getPlayersByWeek(weekId).get(playerId);
		return gameMap;
	}
	
	public String getPicksByGame(String weekId, String playerId, String gameId)
	{
		String pickId  = getGamesByPlayer(weekId, playerId).get(gameId);
		return pickId;
	}

	public Pick createPick(Pick pick)
	{
		String weekId = pick.getWeekId();
		String playerId = pick.getPlayerId();
		String gameId = pick.getGameId();
		String pickId = pick.getId();
		
		Map<String, Map<String, String>> playerMap = getPlayersByWeek(weekId);
		if (playerMap==null)
			playerMap = new HashMap<>();
		
		Map<String, String> gameMap = playerMap.get(playerId);
		if (gameMap == null)
			gameMap = new HashMap<>();
			
		gameMap.put(gameId, pickId);
		playerMap.put(playerId, gameMap);
		
		hashOps.put(key, weekId, playerMap);
		return pick;
	}
}

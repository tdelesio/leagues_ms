package com.makeurpicks.repository.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.KeyValue;
import com.makeurpicks.repository.PlayersInLeagueRepository;

public class RedisPlayersInLeagueRespository extends AbstractRedisCRUDRepository<KeyValue> implements PlayersInLeagueRepository {

	public RedisPlayersInLeagueRespository(RedisTemplate<String, KeyValue> redisTemplate)
	{
		super(redisTemplate);
	}
	
	@Override
	public String getKey() {
		return "players_in_leagues";
	}

	@Override
	public void addPlayerToLeague(String leagueId, String playerId) {
		KeyValue keyValue = findOne(leagueId);
		if (keyValue == null)
		{
			keyValue = new KeyValue();
		}
		
	
		Set<String> list = keyValue.getList();
		if (list == null)
			list = new HashSet<String>();
		
		list.add(playerId);
		keyValue.setId(leagueId);
		keyValue.setList(list);
		
		save(keyValue);
	}

	
	public void removePlayerFromLeague(String leagueId, String playerId) {
		KeyValue keyValue = findOne(leagueId);
		if (keyValue == null)
			return;
		
		Set<String> list = keyValue.getList();
		if (list == null)
			return;
		
		list.remove(playerId);
		
		save(keyValue);
	}
	
}

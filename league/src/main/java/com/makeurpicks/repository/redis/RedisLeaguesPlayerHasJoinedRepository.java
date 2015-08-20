package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.KeyValue;
import com.makeurpicks.repository.LeaguesAPlayHasJoinedRespository;

public class RedisLeaguesPlayerHasJoinedRepository extends AbstractRedisCRUDRepository<KeyValue> implements LeaguesAPlayHasJoinedRespository {


	public RedisLeaguesPlayerHasJoinedRepository(RedisTemplate<String, KeyValue> redisTemplate)
	{
		super(redisTemplate);
	}

	@Override
	public String getKey() {
		return "leagues_player_joined";
	}

	@Override
	public void addPlayerToLeague(String leagueId, String playerId) {
		KeyValue keyValue = findOne(playerId);
		if (keyValue == null)
		{
			keyValue = new KeyValue();
		}
		
	
		Set<String> list = keyValue.getList();
		if (list == null)
			list = new HashSet<String>();
		
		list.add(leagueId);
		keyValue.setList(list);
		keyValue.setId(playerId);
		
		save(keyValue);
	}

	@Override
	public void removePlayerFromLeague(String leagueId, String playerId) {
		KeyValue keyValue = findOne(playerId);
		if (keyValue == null)
			return;
		
		Set<String> list = keyValue.getList();
		if (list == null)
			return;
		
		list.remove(leagueId);
		
		save(keyValue);
	}
	
	
}

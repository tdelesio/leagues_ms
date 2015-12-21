package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.PlayerResponse;
import com.makeurpicks.domain.PlayersInLeague;
import com.makeurpicks.repository.PlayersInLeagueRepository;

public class RedisPlayersInLeagueRespository implements PlayersInLeagueRepository {

	protected final HashOperations<String, String, PlayersInLeague> hashOps;
	
	private Log log = LogFactory.getLog(RedisPlayersInLeagueRespository.class);
	
	public RedisPlayersInLeagueRespository(RedisTemplate<String, PlayersInLeague> redisTemplate)
	{
		this.hashOps = redisTemplate.opsForHash();
	}
	
	public String KEY = "players_in_leagues";


	@Override
	public void addPlayerToLeague(PlayerResponse player, String leagueId) {
		
		log.debug("PlayerResponse="+player+" leagueId="+leagueId);
		
		PlayersInLeague playersInLeague = findOne(leagueId);
		if (playersInLeague == null)
		{
			playersInLeague = new PlayersInLeague();
			playersInLeague.setId(leagueId);
		}
		
		playersInLeague.addPlayer(player);
		save(playersInLeague);
	}

	
	public void removePlayerFromLeague(PlayerResponse player, String leagueId) {
		PlayersInLeague playersInLeague = findOne(leagueId);
		if (playersInLeague == null)
			return;
		
		playersInLeague.removePlayer(player);
		
		save(playersInLeague);
	}
	
	public long count() {
		return hashOps.keys(KEY).size();
	}

	public void delete(String id) {
		hashOps.delete(KEY, id);
	}

	public void delete(PlayersInLeague league) {
		hashOps.delete(KEY, league.getId());
	}

	public void delete(Iterable<? extends PlayersInLeague> leagues) {
		for (PlayersInLeague league : leagues) {
			delete(league);
		}
	}

	public void deleteAll() {
		Set<String> ids = hashOps.keys(KEY);
		for (String id : ids) {
			delete(id);
		}
	}

	public boolean exists(String id) {
		return hashOps.hasKey(KEY, id);
	}

	public Iterable<PlayersInLeague> findAll() {
		return hashOps.values(KEY);
	}

	public Iterable<PlayersInLeague> findAll(Iterable<String> ids) {
		return hashOps.multiGet(KEY, convertIterableToList(ids));
	}

	public PlayersInLeague findOne(String id) {
		return hashOps.get(KEY, id);
	}

	public <S extends PlayersInLeague> S save(S league) {
		hashOps.put(KEY, league.getId(), league);
		return league;
	}

	public <S extends PlayersInLeague> Iterable<S> save(Iterable<S> leagues) {
		List<S> result = new ArrayList<S>();
		for (S entity : leagues) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	private <PlayersInLeague> List<PlayersInLeague> convertIterableToList(Iterable<PlayersInLeague> iterable) {
		List<PlayersInLeague> list = new ArrayList<PlayersInLeague>();
		for (PlayersInLeague object : iterable) {
			list.add(object);
		}
		return list;
	}
}

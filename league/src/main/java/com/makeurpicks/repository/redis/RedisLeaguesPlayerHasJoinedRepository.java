package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.LeaguesPlayerJoined;
import com.makeurpicks.repository.LeaguesAPlayHasJoinedRespository;

public class RedisLeaguesPlayerHasJoinedRepository implements LeaguesAPlayHasJoinedRespository {


	protected final HashOperations<String, String, LeaguesPlayerJoined> hashOps;
	
	public RedisLeaguesPlayerHasJoinedRepository(RedisTemplate<String, LeaguesPlayerJoined> redisTemplate)
	{
		this.hashOps = redisTemplate.opsForHash();
	}

	public String KEY = "leagues_player_joined";

	@Override
	public void addPlayerToLeague(League leauge, String playerId) {
		LeaguesPlayerJoined leaguesPlayerJoined = findOne(playerId);
		
		if (leaguesPlayerJoined == null)
		{
			leaguesPlayerJoined = new LeaguesPlayerJoined();
			leaguesPlayerJoined.setId(playerId);
		}
		
		LeagueName leagueName = new LeagueName();
		leagueName.setLeagueId(leauge.getId());
		leagueName.setSeasonId(leauge.getSeasonId());
		leagueName.setLeagueName(leauge.getLeagueName());
		leaguesPlayerJoined.addLeagueName(leagueName);
		
		save(leaguesPlayerJoined);
	}

	@Override
	public void removePlayerFromLeague(League league, String playerId) {
		LeaguesPlayerJoined leaguesPlayerJoined = findOne(playerId);
		if (leaguesPlayerJoined == null)
			return;
		
		LeagueName leagueName = new LeagueName();
		leagueName.setLeagueId(league.getId());
		leagueName.setLeagueName(league.getLeagueName());
		leagueName.setSeasonId(league.getSeasonId());
		leaguesPlayerJoined.removeLeagueName(leagueName);
		
		save(leaguesPlayerJoined);
	}
	
	public long count() {
		return hashOps.keys(KEY).size();
	}

	public void delete(String id) {
		hashOps.delete(KEY, id);
	}

	public void delete(LeaguesPlayerJoined league) {
		hashOps.delete(KEY, league.getId());
	}

	public void delete(Iterable<? extends LeaguesPlayerJoined> leagues) {
		for (LeaguesPlayerJoined league : leagues) {
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

	public Iterable<LeaguesPlayerJoined> findAll() {
		return hashOps.values(KEY);
	}

	public Iterable<LeaguesPlayerJoined> findAll(Iterable<String> ids) {
		return hashOps.multiGet(KEY, convertIterableToList(ids));
	}

	public LeaguesPlayerJoined findOne(String id) {
		return hashOps.get(KEY, id);
	}

	public <S extends LeaguesPlayerJoined> S save(S league) {
		hashOps.put(KEY, league.getId(), league);
		return league;
	}

	public <S extends LeaguesPlayerJoined> Iterable<S> save(Iterable<S> leagues) {
		List<S> result = new ArrayList<S>();
		for (S entity : leagues) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	private <LeaguesPlayerJoined> List<LeaguesPlayerJoined> convertIterableToList(Iterable<LeaguesPlayerJoined> iterable) {
		List<LeaguesPlayerJoined> list = new ArrayList<LeaguesPlayerJoined>();
		for (LeaguesPlayerJoined object : iterable) {
			list.add(object);
		}
		return list;
	}
	
}

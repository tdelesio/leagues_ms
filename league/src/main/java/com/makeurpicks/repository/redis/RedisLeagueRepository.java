package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.League;
import com.makeurpicks.repository.LeagueRepository;

public class RedisLeagueRepository implements LeagueRepository {

	
	protected final HashOperations<String, String, League> hashOps;
	public static final String LEAGUES_KEY = "leagues";
	
	public RedisLeagueRepository(RedisTemplate<String, League> redisTemplate)
	{
		this.hashOps = redisTemplate.opsForHash();
	}



	public long count() {
		return hashOps.keys(LEAGUES_KEY).size();
	}

	public void delete(String id) {
		hashOps.delete(LEAGUES_KEY, id);
	}

	public void delete(League league) {
		hashOps.delete(LEAGUES_KEY, league.getId());
	}

	public void delete(Iterable<? extends League> leagues) {
		for (League league : leagues) {
			delete(league);
		}
	}

	public void deleteAll() {
		Set<String> ids = hashOps.keys(LEAGUES_KEY);
		for (String id : ids) {
			delete(id);
		}
	}

	public boolean exists(String id) {
		return hashOps.hasKey(LEAGUES_KEY, id);
	}

	public Iterable<League> findAll() {
		return hashOps.values(LEAGUES_KEY);
	}

	public Iterable<League> findAll(Iterable<String> ids) {
		return hashOps.multiGet(LEAGUES_KEY, convertIterableToList(ids));
	}

	public League findOne(String id) {
		return hashOps.get(LEAGUES_KEY, id);
	}

	public <S extends League> S save(S league) {
		hashOps.put(LEAGUES_KEY, league.getId(), league);
		return league;
	}

	public <S extends League> Iterable<S> save(Iterable<S> leagues) {
		List<S> result = new ArrayList<S>();
		for (S entity : leagues) {
			save(entity);
			result.add(entity);
		}
		return result;
	}

	private <League> List<League> convertIterableToList(Iterable<League> iterable) {
		List<League> list = new ArrayList<League>();
		for (League object : iterable) {
			list.add(object);
		}
		return list;
	}

}

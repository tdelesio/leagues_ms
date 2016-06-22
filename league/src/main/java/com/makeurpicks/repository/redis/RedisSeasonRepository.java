package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Season;
import com.makeurpicks.repository.SeasonRepository;

public class RedisSeasonRepository implements SeasonRepository {

	//protected final HashOperations<String, String, Season> hashOps;
	private RedisTemplate<String, Season> template;
	
	public RedisSeasonRepository(RedisTemplate<String, Season> redisTemplate)
	{
//		this.hashOps = redisTemplate.opsForHash();
		template = redisTemplate;
	}
	
	public String KEY = "seasons";


	public List<Season> getSeasonsByLeagueType(String leaueType)
	{
		List<Object> allSeasons = findAll();
		List<Season> seasons = new ArrayList<Season>();
		for (Object o:allSeasons)
		{
			Season season = (Season)o;
			if (season.getLeagueType().equals(leaueType))
				seasons.add(season);
		}
		
		return seasons;
	}
	
//	public long count() {
//		return hashOps.keys(KEY).size();
//	}
//
//	public void delete(String id) {
//		hashOps.delete(KEY, id);
//	}
//
	public void delete(String seasonId) {
		template.opsForHash().delete(KEY, seasonId);
	}

//	public void delete(Iterable<? extends Season> leagues) {
//		for (Season league : leagues) {
//			delete(league);
//		}
//	}
//
//	public void deleteAll() {
//		Set<String> ids = hashOps.keys(KEY);
//		for (String id : ids) {
//			delete(id);
//		}
//	}

//	public boolean exists(String id) {
//		return hashOps.hasKey(KEY, id);
//	}
//
		public List<Object> findAll() {
			return template.opsForHash().values(KEY);
		}
//
//	public Iterable<Season> findAll(Iterable<String> ids) {
//		return hashOps.multiGet(KEY, convertIterableToList(ids));
//	}
//
//	public Season findOne(String id) {
//		return hashOps.get(KEY, id);
//	}
//
	public Season save(Season season) {
		template.opsForHash().put(KEY, season.getId(), season);
		return season;
	}

//	public <S extends Season> Iterable<S> save(Iterable<S> leagues) {
//		List<S> result = new ArrayList<S>();
//		for (S entity : leagues) {
//			save(entity);
//			result.add(entity);
//		}
//		return result;
//	}
//
//	private <Season> List<Season> convertIterableToList(Iterable<Season> iterable) {
//		List<Season> list = new ArrayList<Season>();
//		for (Season object : iterable) {
//			list.add(object);
//		}
//		return list;
//	}
	

	
	
}

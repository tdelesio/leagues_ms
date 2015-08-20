package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Season;
import com.makeurpicks.repository.SeasonRepository;

public class RedisSeasonRepository extends AbstractRedisSetCRUDRepository<Season>
		implements SeasonRepository {

	public RedisSeasonRepository(RedisTemplate<String, Season> redisTemplate)
	{
		super(redisTemplate);
	}
	
	public final static String key_prefix = "SEASON_BY_LEAGUE_TYPLE-";
	
	private String buildKey(String leaugeType)
	{
		return key_prefix+leaugeType;
	}
	
	public Season createUpdateSeason(Season season)
	{
		add(buildKey(season.getLeagueType()), season);
		return season;
	}
	
	public Iterable<Season> getSeasonsByLeagueType(String leaueType)
	{
		return findAll(buildKey(leaueType));
	}
	

	
}

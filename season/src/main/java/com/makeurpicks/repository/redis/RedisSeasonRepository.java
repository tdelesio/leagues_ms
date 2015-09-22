package com.makeurpicks.repository.redis;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Season;
import com.makeurpicks.repository.SeasonRepository;

public class RedisSeasonRepository extends AbstractRedisCRUDRepository<Season>
		implements SeasonRepository {

	public RedisSeasonRepository(RedisTemplate<String, Season> redisTemplate)
	{
		super(redisTemplate);
	}
	
	@Override
	public String getKey() {
		return "SEASONS";
	}


	public List<Season> getSeasonsByLeagueType(String leaueType)
	{
		Iterable<Season> allSeasons = findAll();
		List<Season> seasons = new ArrayList<Season>();
		for (Season season:allSeasons)
		{
			if (season.getLeagueType().equals(leaueType))
				seasons.add(season);
		}
		
		return seasons;
	}
	

	
	
}

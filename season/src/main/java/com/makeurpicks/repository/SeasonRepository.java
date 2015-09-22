package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.Season;

public interface SeasonRepository extends CrudRepository<Season, String>{

	public List<Season> getSeasonsByLeagueType(String leaueType);
	
}

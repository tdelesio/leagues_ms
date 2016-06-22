package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.makeurpicks.domain.Season;

public interface SeasonRepository extends Repository<Season, String> {//CrudRepository<Season, String>{

	public List<Season> getSeasonsByLeagueType(String leaueType);
	public Season save(Season season);
	public void delete(String seasonId);
	
}

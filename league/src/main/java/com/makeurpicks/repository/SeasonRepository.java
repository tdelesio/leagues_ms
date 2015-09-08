package com.makeurpicks.repository;

import com.makeurpicks.domain.Season;

public interface SeasonRepository {

	public Season createUpdateSeason(Season season);
	public Iterable<Season> getSeasonsByLeagueType(String leaueType);
	
}

package com.makeurpicks.repository;

import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.Team;

public interface SeasonRepository {

	public Season createUpdateSeason(Season season);
	public Iterable<Season> getSeasonsByLeagueType(String leaueType);
	
}

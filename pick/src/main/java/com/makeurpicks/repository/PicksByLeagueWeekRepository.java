package com.makeurpicks.repository;

import com.makeurpicks.domain.Pick;


public interface PicksByLeagueWeekRepository {

	public void addPick(Pick pick);
	public Iterable<String> getPicksForLeagueAndWeek(String leagueId, String weekId);
}

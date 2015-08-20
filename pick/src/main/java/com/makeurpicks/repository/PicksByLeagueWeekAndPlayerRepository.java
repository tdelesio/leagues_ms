package com.makeurpicks.repository;

import com.makeurpicks.domain.Pick;


public interface PicksByLeagueWeekAndPlayerRepository {

	public void addPick(Pick pick);
	public Iterable<String> getPicksForLeagueWeekAndPlayer(String leagueId, String weekId, String player);
}

package com.makeurpicks.repository;

import com.makeurpicks.domain.Team;

public interface TeamRepository {

	public Team createUpdateTeam(Team team);
	public Iterable<Team> getTeamsByLeagueType(String leaueType);
	
}

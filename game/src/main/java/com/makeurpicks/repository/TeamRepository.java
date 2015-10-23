package com.makeurpicks.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.makeurpicks.domain.Team;

@Component
public class TeamRepository {

	private static Map<String, Team> teams = new HashMap<>(33);
	private static Map<String, List<Team>> teamsByLeagueType = new HashMap<>(3);
	
	public void save(Team team)
	{
		teams.put(team.getId(), team);
		
		List<Team> allTeamsInLeagueType = teamsByLeagueType.get(team.getLeagueType());
		if (allTeamsInLeagueType == null)
			allTeamsInLeagueType = new ArrayList<Team>();
		
		allTeamsInLeagueType.add(team);
		
		teamsByLeagueType.put(team.getLeagueType(), allTeamsInLeagueType);
	}
	
	public Team findOne(String id)
	{
		return teams.get(id);
	}
	
	public List<Team> getTeamsByLeagueType(String leagueType)
	{
		return teamsByLeagueType.get(leagueType);
	}
}

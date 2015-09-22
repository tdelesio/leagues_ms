package com.makeurpicks.repository;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.Team;

public interface TeamRepository extends CrudRepository<Team, String>{

	public Map<String, Team> getTeamsByLeagueType(String leagueType);
	
}

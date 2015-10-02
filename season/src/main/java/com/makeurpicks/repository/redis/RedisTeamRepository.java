package com.makeurpicks.repository.redis;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Team;
import com.makeurpicks.repository.TeamRepository;

public class RedisTeamRepository extends AbstractRedisCRUDRepository<Team>
		implements TeamRepository {

	private static Map<String, Map<String, Team>> allTeams = null;
	
	public RedisTeamRepository(RedisTemplate<String, Team> redisTemplate)
	{
		super(redisTemplate);
	}
	

	@Override
	public String getKey() {
		return "TEAMS";
	}
	
	public Map<String, Team> getTeamsByLeagueType(String leagueType)
	{
		Map<String, Team> teamByLeagueType;
		if (allTeams == null)
		{
			allTeams = new HashMap<String, Map<String,Team>>();
		}
		
		teamByLeagueType = allTeams.get(leagueType);
		if (teamByLeagueType == null || teamByLeagueType.isEmpty())
			teamByLeagueType = new HashMap<String, Team>();
		else
			return teamByLeagueType;
			
		Iterable<Team> teams = findAll();
			
		for (Team team:teams)
		{
			if (leagueType.equalsIgnoreCase(team.getLeagueType()))
				teamByLeagueType.put(team.getId(), team);
		}
		
		allTeams.put(leagueType, teamByLeagueType);
		
		return teamByLeagueType;
	}

	

	
}
;
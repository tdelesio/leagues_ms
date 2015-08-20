package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Team;
import com.makeurpicks.repository.TeamRepository;

public class RedisTeamRepository extends AbstractRedisSetCRUDRepository<Team>
		implements TeamRepository {

	public RedisTeamRepository(RedisTemplate<String, Team> redisTemplate)
	{
		super(redisTemplate);
	}
	
	public final static String key_prefix = "TEAM_BY_LEAGUE_TYPLE-";
	
	private String buildKey(String leaugeType)
	{
		return key_prefix+leaugeType;
	}
	
	public Team createUpdateTeam(Team team)
	{
		add(buildKey(team.getLeagueType()), team);
		return team;
	}
	
	public Iterable<Team> getTeamsByLeagueType(String leaueType)
	{
		return findAll(buildKey(leaueType));
	}
	

	
}

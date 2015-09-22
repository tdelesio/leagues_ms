package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.LeaguesPlayerJoined;
import com.makeurpicks.repository.LeaguesAPlayHasJoinedRespository;

public class RedisLeaguesPlayerHasJoinedRepository extends AbstractRedisCRUDRepository<LeaguesPlayerJoined> implements LeaguesAPlayHasJoinedRespository {


	public RedisLeaguesPlayerHasJoinedRepository(RedisTemplate<String, LeaguesPlayerJoined> redisTemplate)
	{
		super(redisTemplate);
	}

	@Override
	public String getKey() {
		return "leagues_player_joined";
	}

	@Override
	public void addPlayerToLeague(League leauge, String playerId) {
		LeaguesPlayerJoined leaguesPlayerJoined = findOne(playerId);
		
		if (leaguesPlayerJoined == null)
		{
			leaguesPlayerJoined = new LeaguesPlayerJoined();
			leaguesPlayerJoined.setId(playerId);
		}
		
		LeagueName leagueName = new LeagueName();
		leagueName.setLeagueId(leauge.getId());
		leagueName.setLeagueName(leauge.getLeagueName());
		leaguesPlayerJoined.addLeagueName(leagueName);
		
		save(leaguesPlayerJoined);
	}

	@Override
	public void removePlayerFromLeague(League league, String playerId) {
		LeaguesPlayerJoined leaguesPlayerJoined = findOne(playerId);
		if (leaguesPlayerJoined == null)
			return;
		
		LeagueName leagueName = new LeagueName();
		leagueName.setLeagueId(league.getId());
		leagueName.setLeagueName(league.getLeagueName());
		leaguesPlayerJoined.removeLeagueName(leagueName);
		
		save(leaguesPlayerJoined);
	}
	
	
}

package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.PlayerResponse;
import com.makeurpicks.domain.PlayersInLeague;
import com.makeurpicks.repository.PlayersInLeagueRepository;

public class RedisPlayersInLeagueRespository extends AbstractRedisCRUDRepository<PlayersInLeague> implements PlayersInLeagueRepository {

	public RedisPlayersInLeagueRespository(RedisTemplate<String, PlayersInLeague> redisTemplate)
	{
		super(redisTemplate);
	}
	
	@Override
	public String getKey() {
		return "players_in_leagues";
	}

	@Override
	public void addPlayerToLeague(PlayerResponse player, String leagueId) {
		PlayersInLeague playersInLeague = findOne(leagueId);
		if (playersInLeague == null)
		{
			playersInLeague = new PlayersInLeague();
			playersInLeague.setId(leagueId);
		}
		
		playersInLeague.addPlayer(player);
		save(playersInLeague);
	}

	
	public void removePlayerFromLeague(PlayerResponse player, String leagueId) {
		PlayersInLeague playersInLeague = findOne(leagueId);
		if (playersInLeague == null)
			return;
		
		playersInLeague.removePlayer(player);
		
		save(playersInLeague);
	}
	
}

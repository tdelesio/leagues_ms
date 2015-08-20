package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.KeyValue;

public interface PlayersInLeagueRepository extends CrudRepository<KeyValue, String>
{
	public void addPlayerToLeague(String playerId, String leagueId);
	public void removePlayerFromLeague(String playerId, String leagueId);
}
 
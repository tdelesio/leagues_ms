package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.PlayersInLeague;

public interface PlayersInLeagueRepository extends CrudRepository<PlayersInLeague, String>
{
	public void addPlayerToLeague(String playerId, String leagueId);
	public void delete(String leagueId);
}
 
package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.PlayerResponse;
import com.makeurpicks.domain.PlayersInLeague;

public interface PlayersInLeagueRepository extends CrudRepository<PlayersInLeague, String>
{
	public void addPlayerToLeague(PlayerResponse player, String leagueId);
	public void removePlayerFromLeague(PlayerResponse player, String leagueId);
}
 
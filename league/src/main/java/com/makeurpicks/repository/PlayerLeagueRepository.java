package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.PlayerLeague;

public interface PlayerLeagueRepository extends CrudRepository<PlayerLeague, String> {

	@Query(value="select pl.league from PlayerLeague pl  join pl.league  where pl.playerId = ?")
	public List<League> findLeaguesForPlayer(String playerId);
	
	@Query(value="select id from PlayerLeague pl where pl.league.id = ?")
	public List<String> findPlayersForLeague(String playerId);
}

package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.PlayerLeague;

public interface PlayerLeagueRepository extends CrudRepository<PlayerLeague, String> {

	public List<League> findLeaguesByPlayerId(String playerId);
	
	/*@Query(value="select id from PlayerLeague pl where pl.league.id = ?")
	public List<String> findPlayersForLeague(String playerId);*/
}

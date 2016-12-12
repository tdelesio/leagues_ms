package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.PlayerLeague;

public interface PlayerLeagueRepository extends CrudRepository<PlayerLeague, String> {

	@Query(value="select League from PlayerLeague pl inner join League l on pl.leagueId = l.id where pl.playerId = ?")
	public List<League> findLeaguesForPlayer(String playerId);
	
	@Query(value="select id from PlayerLeague pl where pl.leagueId = ?")
	public List<String> findPlayersForLeague(String playerId);
}

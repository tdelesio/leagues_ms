package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.PlayerLeague;
@Repository
public interface PlayerLeagueRepository extends CrudRepository<PlayerLeague, String> {

	public List<Integer> findLeagueIdsByPlayerId(String playerId);
	public int findIdByPlayerId(String playerId);
	public List<String> findPlayerIdsByLeagueId(String leagueId);
	public PlayerLeague findByLeagueIdAndPlayerId(Integer leagueId,String playerId);
	
	/*@Query(value="select id from PlayerLeague pl where pl.league.id = ?")
	public List<String> findPlayersForLeague(String playerId);*/
}

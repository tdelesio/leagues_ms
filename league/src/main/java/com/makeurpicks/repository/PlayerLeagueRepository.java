package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.makeurpicks.domain.PlayerLeague;
@Repository
public interface PlayerLeagueRepository extends JpaRepository<PlayerLeague, String> {
	@Query(value="select id.leagueId from PlayerLeague pl where pl.id.playerId = ?")
	public List<String> findIdLeagueIdsByIdPlayerId(String playerId);
	@Query(value="select id.playerId from PlayerLeague pl where pl.id.leagueId = ?")
	public List<String> findIdPlayerIdsByIdLeagueId(String leagueId);
	public PlayerLeague findByIdLeagueIdAndIdPlayerId(String leagueId,String playerId);
	
	/*@Query(value="select id from PlayerLeague pl where pl.league.id = ?")
	public List<String> findPlayersForLeague(String playerId);*/
}

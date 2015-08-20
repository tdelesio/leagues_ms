package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.KeyValue;

public interface LeaguesAPlayHasJoinedRespository extends CrudRepository<KeyValue, String>{


	public void addPlayerToLeague(String leagueId, String playerId);
	public void removePlayerFromLeague(String leagueId, String playerId);
}
 
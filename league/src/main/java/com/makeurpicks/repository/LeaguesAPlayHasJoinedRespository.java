package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeaguesPlayerJoined;

public interface LeaguesAPlayHasJoinedRespository extends CrudRepository<LeaguesPlayerJoined, String>{


	public void addPlayerToLeague(League leauge, String playerId);
	public void removePlayerFromLeague(League league, String playerId);
}
 
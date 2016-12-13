package com.makeurpicks.repository;

import java.util.Map;

import org.springframework.data.repository.Repository;

import com.makeurpicks.domain.Pick;

//	WeekId, PlayerId, GameId, PickId
//	Map<String, Map<String, String>>
//  <Map<String, Map<String, String>>, String>
public interface PicksByWeekRepository extends Repository<Map<String, Map<String, Map<String, String>>>, String> {
//extends CrudRepository<Map<String, Map<String, String>>, String>{

	public Map<String, Map<String, String>> getPlayersByWeek(String leagueId, String weekId);
	public Map<String, Map<String, Map<String, String>>> getWeeksByLeague(String leagueId);
	public Map<String, String> getGamesByPlayer(String leagueId, String weekId, String playerId);
	public String getPicksByGame(String leagueId, String weekId, String playerId, String gameId);
	public Pick createPick(Pick pick);
	public void deleteAll();
	
}

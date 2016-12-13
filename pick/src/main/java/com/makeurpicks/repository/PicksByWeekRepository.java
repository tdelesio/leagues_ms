package com.makeurpicks.repository;

import java.util.Map;

import org.springframework.data.repository.Repository;

import com.makeurpicks.domain.Pick;

//	WeekId, PlayerId, GameId, PickId
//	Map<String, Map<String, String>>
//  <Map<String, Map<String, String>>, String>
public interface PicksByWeekRepository extends Repository<Map<String, Map<String, Map<String, String>>>, String> {
//extends CrudRepository<Map<String, Map<String, String>>, String>{

<<<<<<< HEAD
	public Map<String, Map<String, String>> getPlayersByWeek(String leagueId, String weekId);
	public Map<String, Map<String, Map<String, String>>> getWeeksByLeague(String leagueId);
	public Map<String, String> getGamesByPlayer(String leagueId, String weekId, String playerId);
	public String getPicksByGame(String leagueId, String weekId, String playerId, String gameId);
=======
	public Map<String, Map<String, Map<String, String>>> findWeeksByLeague(String leagueId);
	public Map<String, Map<String, String>> findPlayersByWeek(String leagueId, String weekId);
	public Map<String, String> findGamesByPlayer(String leagueId, String weekId, String playerId);
	public String findPicksByGame(String leagueId, String weekId, String playerId, String gameId);
>>>>>>> bf341f89a56ff883f6f8bc3693fb994ea602156d
	public Pick createPick(Pick pick);
	public void deleteAll();
	
}

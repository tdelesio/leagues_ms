package com.makeurpicks;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.Team;
import com.makeurpicks.domain.Week;
import com.makeurpicks.service.GameService;

@RestController
@RequestMapping(produces = "application/json", consumes = "application/json")
public class GameController {

	@Autowired
	private GameService gameService;
	
	@RequestMapping(method=RequestMethod.GET, value="/week/seasonId/{id}")
	public @ResponseBody Iterable<Week> getWeeksBySeason(@PathParam("id")String seasonId)
	{
		return gameService.getWeeksBySeason(seasonId);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/week")
	public @ResponseBody Week createWeek(@RequestBody Week week)
	{
		return gameService.createWeek(week);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/season/current")
	public @ResponseBody List<Season> getCurrentSeasons()
	{
		return gameService.getCurrentSeasons();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/season")
	public @ResponseBody Season createSeason(@RequestBody Season season)
	{
		return gameService.createUpdateSeason(season);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/season")
	public @ResponseBody Season updateSeason(@RequestBody Season season)
	{
		return gameService.createUpdateSeason(season);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/leaguetype")
	public @ResponseBody LeagueType[] getLeagueType()
	{
		return gameService.getLeagueType();
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/team/leaguetype/{lt}")
	public @ResponseBody Iterable<Team> getTeams(@PathParam("lt")String leagueType)
	{
		return gameService.getTeams(leagueType);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/game/{id}")
	public @ResponseBody Game getGameById(@PathParam("id")String gameId)
	{
		return gameService.getGameById(gameId);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/game")
	public @ResponseBody Game createGame(@RequestBody Game game)
	{
		return gameService.createGame(game);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/game")
	public @ResponseBody Game updateGame(Game game)
	{
		return gameService.updateGame(game);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/game/weekid/{id}")
	public @ResponseBody Iterable<Game> getGamesByWeek(@PathParam("id") String weekId)
	{
		return gameService.getGamesByWeek(weekId);
	}
}

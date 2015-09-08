package com.makeurpicks;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.Team;
import com.makeurpicks.domain.Week;
import com.makeurpicks.service.GameService;

@RestController
@RequestMapping(value="/game", produces = MediaType.APPLICATION_JSON, consumes = {MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
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

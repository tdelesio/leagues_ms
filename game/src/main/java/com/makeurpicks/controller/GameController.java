package com.makeurpicks.controller;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.Week;
import com.makeurpicks.service.GameService;

@RestController
@RequestMapping(value="/game")
public class GameController {

	@Autowired
	private GameService gameService;
	
	@RequestMapping(method=RequestMethod.POST, value="/game")
	public @ResponseBody Game createGame(@RequestBody Game game)
	{
		return gameService.createGame(game);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/game")
	public @ResponseBody Game updateGame(@RequestBody Game game)
	{
		return gameService.updateGame(game);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/game/weekid/{id}")
	public @ResponseBody Iterable<Game> getGamesByWeek(@PathVariable String id)
	{
		return gameService.getGamesByWeek(id);
	}
}

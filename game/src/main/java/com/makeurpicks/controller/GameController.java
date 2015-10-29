package com.makeurpicks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.NFLWeek;
import com.makeurpicks.service.GameService;

@RestController
@RequestMapping(value="/games")
public class GameController {

	@Autowired
	private GameService gameService;
	
//	spread":3.5,"date":"2015-10-18T23:49:07.978Z","time":"1970-01-01T18:00:00.000Z","
//			+ ""favId":"-7994476373137338968-5749449990071436219","
//					+ ""dogid":"-8180369574573424501-5773632938726789358","
//							+ ""seasonId":"-4276273245322657635-7137201729840442652","
//									+ ""weekId":"2329348844980554530-5161211727328479683","
//											+ ""dogId":"-8180369574573424501-5773632938726789358"
	
//	@InitBinder
//	private void dateBinder(WebDataBinder binder) {
//	            //The date format to parse or output your dates
////		2015-10-25T20:18:47.031Z
//	    SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DDTHH:mm:ss.sssZ");
//	            //Create a new CustomDateEditor
//	    CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
//	            //Register it as custom editor for the Date type
//	    binder.registerCustomEditor(LocalDateTime.class, editor);
//	}
	
	
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Game createGame(@RequestBody Game game)
	{
		return gameService.createGame(game);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/")
	public @ResponseBody Game updateGame(@RequestBody Game game)
	{
		return gameService.updateGame(game);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/score")
	public @ResponseBody Game updateGameScore(@RequestBody Game game)
	{
		return gameService.updateGameScore(game);
	}
	
	
	@RequestMapping(method=RequestMethod.GET, value="/weekid/{id}")
	public @ResponseBody Iterable<Game> getGamesByWeek(@PathVariable String id)
	{
		return gameService.getGamesByWeek(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public @ResponseBody Game getGame(@PathVariable String id)
	{
		Game game =  gameService.getGameById(id);
		return game;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/autosetup")
	public @ResponseBody NFLWeek callNFLandSetupWeek()
	{
		return gameService.loadFromNFL();
	}
}

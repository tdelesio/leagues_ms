package com.makeurpicks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.Week;
import com.makeurpicks.exception.GameValidationException;
import com.makeurpicks.exception.GameValidationException.GameExceptions;
import com.makeurpicks.repository.GameRepository;
import com.makeurpicks.repository.WeekRepository;

@Component
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	public Game createGame(Game game)
	{
		validateGame(game);
		
		return gameRepository.save(game);
	}
	
	public Game updateGame(Game game)
	{
		validateGame(game);
		
		return gameRepository.save(game);
	}
	
	public List<Game> getGamesByWeek(String weekId)
	{
		return gameRepository.findByWeekId(weekId);
	}
	
	public Game getGameById(String gameId)
	{
		return gameRepository.findOne(gameId);
	}
	
	private void validateGame(Game game)
	{
		if (game==null)
			throw new GameValidationException(GameExceptions.GAME_IS_NULL);
		if ("".equals(game.getFavId()))
			throw new GameValidationException(GameExceptions.FAVORITE_IS_NULL);
		if ("".equals(game.getDogId()))
			throw new GameValidationException(GameExceptions.DOG_IS_NULL);
		if ("".equals(game.getWeekId()))
			throw new GameValidationException(GameExceptions.WEEK_IS_NULL);
		if (game.getGameStart() == 0)
			throw new GameValidationException(GameExceptions.GAMESTART_IS_NULL);
		
		if (game.getFavId().equals(game.getDogId()))
			throw new GameValidationException(GameExceptions.TEAM_CANNOT_PLAY_ITSELF);
		
		
	}
}

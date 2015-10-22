package com.makeurpicks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.Team;
import com.makeurpicks.exception.GameValidationException;
import com.makeurpicks.exception.GameValidationException.GameExceptions;
import com.makeurpicks.repository.GameRepository;

@Component
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private TeamService teamService;
	
	public Game createGame(Game game)
	{
		validateGame(game);
		
		game.generateId();
		
		Team fav = teamService.getTeam(game.getFavId());
		Team dog = teamService.getTeam(game.getDogId());
		game.setFavFullName(fav.getFullTeamName());
		game.setDogFullName(dog.getFullTeamName());
		
		return gameRepository.save(game);
	}
	
	public Game updateGame(Game game)
	{
		validateGame(game);
		
		//allow only certain fields to be updated
		Game gameFromDS = gameRepository.findOne(game.getId());
		gameFromDS.setGameStart(game.getGameStart());
		gameFromDS.setSpread(game.getSpread());
		gameFromDS.setFavHome(game.isFavHome());
		gameFromDS.setFavScore(game.getFavScore());
		gameFromDS.setDogScore(game.getDogScore());
		
		return gameRepository.save(gameFromDS);
	}
	
	public Game updateGameScore(Game game)
	{
		Game gameFromDS = gameRepository.findOne(game.getId());
		gameFromDS.setFavScore(game.getFavScore());
		gameFromDS.setDogScore(game.getDogScore());
		return gameRepository.save(gameFromDS);
	}
	
	public List<Game> getGamesByWeek(String weekId)
	{
		return gameRepository.findByWeekId(weekId);
	}
	
	public Game getGameById(String gameId)
	{
		Game game = gameRepository.findOne(gameId);
		return game;
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
		if (game.getGameStart() == null)
			throw new GameValidationException(GameExceptions.GAMESTART_IS_NULL);
		
		if (game.getFavId().equals(game.getDogId()))
			throw new GameValidationException(GameExceptions.TEAM_CANNOT_PLAY_ITSELF);
		
		
	}
}

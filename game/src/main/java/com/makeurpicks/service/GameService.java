package com.makeurpicks.service;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalField;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.hibernate.id.enhanced.LegacyHiLoAlgorithmOptimizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.GameBuilder;
import com.makeurpicks.domain.NFLGame;
import com.makeurpicks.domain.NFLWeek;
import com.makeurpicks.domain.Team;
import com.makeurpicks.domain.Week;
import com.makeurpicks.domain.WeekBuilder;
import com.makeurpicks.exception.GameValidationException;
import com.makeurpicks.exception.GameValidationException.GameExceptions;
import com.makeurpicks.repository.GameRepository;

@Component
public class GameService {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private WeekService weekService;
		
	public Game createGame(Game game)
	{
		validateGame(game);
		
		game.generateId();
		
		Team fav = teamService.getTeam(game.getFavId());
		Team dog = teamService.getTeam(game.getDogId());
		game.setDogShortName(dog.getShortName());
		game.setFavShortName(fav.getShortName());
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
		List<Game> games = gameRepository.findByWeekId(weekId);
		
		Collections.sort(games, (g1, g2)-> g1.getGameStart().compareTo(g2.getGameStart()));
		return games;
//		return gameRepository.findByWeekIdOrderByGameStart(weekId);
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
	
	
	public NFLWeek loadFromNFL()
	{
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		return restTemplate.getForObject("http://www.nfl.com/liveupdate/scorestrip/ss.json", NFLWeek.class);
	}
	
	public void autoSetupWeek(String seasonId)
	{
		if (seasonId == null || "".equals(seasonId))
			throw new GameValidationException(GameExceptions.SEASON_ID_IS_NULL);
		List<Week> weeks = weekService.getWeeksBySeason(seasonId);
		
		NFLWeek nflWeek = loadFromNFL();
		int weekNumber = nflWeek.getW();
		String foundWeekId = null;
		
		for (Week week: weeks)
		{
			if (week.getWeekNumber() == weekNumber)
				foundWeekId = week.getId();
		}
		
		if (foundWeekId == null)
		{
			//week not found so we need to create one
			Week newWeek = new WeekBuilder().withWeekNumber(weekNumber).withSeasonId(seasonId).build();
			Week week = weekService.createWeek(newWeek);
			
			//now that we have a week setup, let's create the games
			for (NFLGame nflGame:nflWeek.getGms())
			{
				Team home = teamService.getTeamByShortName("pickem", nflGame.getH());
				Team away = teamService.getTeamByShortName("pickem", nflGame.getV());
				String day = nflGame.getD();
				String time = nflGame.getT();
				ZonedDateTime gameStart = ZonedDateTime.now();
				StringTokenizer stringTokenizer = new StringTokenizer(time, ":");
				String hour = stringTokenizer.nextToken();
				String min = stringTokenizer.nextToken();
				gameStart = gameStart
						.withHour(Integer.parseInt(hour)+12)
						.withMinute(Integer.parseInt(min))
						.withSecond(0)
						.withNano(0);
				
				//based on running on wed
				int dayOffSet = 0;
				if (day.equals("Thu"))
					dayOffSet++;
				else if (day.equals("Sat"))
					dayOffSet+=3;
				else if (day.equals("Sun"))
					dayOffSet+=4;
				else if (day.equals("Mon"))
					dayOffSet+=5;
				
				//monday is 1, tues is 2, wed is 3, thur is 4
				int currentDay = gameStart.getDayOfWeek().getValue();
				if (currentDay == 2)
					dayOffSet++;
				else if (currentDay == 4)
					dayOffSet--;
				else if (currentDay == 5)
					dayOffSet-=2;
				
				gameStart = gameStart.plusDays(dayOffSet);
				Game game = new GameBuilder()
						.withFavId(home.getId())
						.withDogId(away.getId())
						.withWeekId(week.getId())
						.withGameStartTime(gameStart)
						.build();
				
				createGame(game);
			}
		}
		
	}
}

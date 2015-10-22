package com.makeurpicks.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.GameApplication;
import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.GameBuilder;
import com.makeurpicks.domain.Week;
import com.makeurpicks.domain.WeekBuilder;
import com.makeurpicks.repository.GameRepository;
import com.makeurpicks.repository.WeekRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GameApplication.class)
public class GameServiceTest {

	@Autowired
	private GameRepository gameRepository;
	
	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private WeekService weekService;
	
	@Before
	public void setup()
	{
		gameRepository.deleteAll();
		weekRepository.deleteAll();
	}
	
	private Week createWeek(String seasonId, int weekNumber)
	{
		Week week = new WeekBuilder()
			.withSeasonId(seasonId)
			.withWeekNumber(weekNumber)
			.build();
		
		week = weekService.createWeek(week);
		
		return week;	
	}
	
	
	

	@Test
	public void testGetGameById() {
		Week week = createWeek("1", 1);
		Game game = createGame(week.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		assertEquals(game, gameService.getGameById(game.getId()));
	}
	
	private Game createGame(String weekId, String fav, String dog, double spread, long start)
	{
		Game game = new GameBuilder()
			.withWeekId(weekId)
			.withFavId(fav)
			.withDogId(dog)
			.withSpread(spread)
			.withGameStartTime(ZonedDateTime.ofInstant(Instant.ofEpochMilli(start), ZoneId.systemDefault()))
			.build();
		
		game = gameService.createGame(game);
		
		return game;
	}

	@Test
	public void testCreateGame() {
		Week week = createWeek("1", 1);
		Game game = createGame(week.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		assertThat(game.getId(), instanceOf(String.class));
		
		assertEquals(game, gameRepository.findOne(game.getId()));
	}

	@Test
	public void testUpdateGame() {
		Week week = createWeek("1", 1);
		Game game = createGame(week.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		game.setFavScore(35);
		game.setDogScore(21);
		game = gameService.updateGame(game);
		
		game = gameService.getGameById(game.getId());
		assertEquals(35, game.getFavScore());
		assertEquals(21, game.getDogScore());
	}

	@Test
	public void testGetGamesByWeek() {
	
		Week week1Season1 = createWeek("1", 1);
		Game game1Week1 = createGame(week1Season1.getId(), "1", "2", 5.5, System.currentTimeMillis());
		Game game2Week1 = createGame(week1Season1.getId(), "1", "2", 5.5, System.currentTimeMillis());
		Game game3Week1 = createGame(week1Season1.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		Week week2Season1 = createWeek("1", 2);
		Game game1Week2 = createGame(week2Season1.getId(), "1", "2", 5.5, System.currentTimeMillis());
		Game game2Week2 = createGame(week2Season1.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		Week week1Season2 = createWeek("2", 1);
		Game game1Week1Season2 = createGame(week1Season2.getId(), "1", "2", 5.5, System.currentTimeMillis());
		Game game2Week1Season2 = createGame(week1Season2.getId(), "1", "2", 5.5, System.currentTimeMillis());
		Game game3Week1Season2 = createGame(week1Season2.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		List<Game> games = gameService.getGamesByWeek(week1Season1.getId());
		assertTrue(games.contains(game1Week1));
		assertTrue(games.contains(game2Week1));
		assertTrue(games.contains(game3Week1));
		assertFalse(games.contains(game1Week2));
		assertFalse(games.contains(game2Week2));
		assertFalse(games.contains(game1Week1Season2));
		assertFalse(games.contains(game2Week1Season2));
		assertFalse(games.contains(game3Week1Season2));
		
		games = gameService.getGamesByWeek(week2Season1.getId());
		assertTrue(games.contains(game1Week2));
		assertTrue(games.contains(game2Week2));
		assertFalse(games.contains(game1Week1));
		assertFalse(games.contains(game2Week1));
		assertFalse(games.contains(game3Week1));
		assertFalse(games.contains(game1Week1Season2));
		assertFalse(games.contains(game2Week1Season2));
		assertFalse(games.contains(game3Week1Season2));
		
		games = gameService.getGamesByWeek(week1Season2.getId());
		assertTrue(games.contains(game1Week1Season2));
		assertTrue(games.contains(game2Week1Season2));
		assertTrue(games.contains(game3Week1Season2));
		assertFalse(games.contains(game1Week1));
		assertFalse(games.contains(game2Week1));
		assertFalse(games.contains(game3Week1));
		assertFalse(games.contains(game1Week2));
		assertFalse(games.contains(game2Week2));
	}

}

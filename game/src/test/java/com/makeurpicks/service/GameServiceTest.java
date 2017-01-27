package com.makeurpicks.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.GameApplication;
import com.makeurpicks.domain.Game;
import com.makeurpicks.domain.GameBuilder;
import com.makeurpicks.domain.Team;
import com.makeurpicks.domain.Week;
import com.makeurpicks.domain.WeekBuilder;
import com.makeurpicks.exception.GameValidationException;
import com.makeurpicks.exception.GameValidationException.GameExceptions;
import com.makeurpicks.repository.GameRepository;
import com.makeurpicks.repository.WeekRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = GameApplication.class)
public class GameServiceTest {

	@Mock
	private GameRepository gameRepository;
	
	
	@Mock
	private WeekRepository weekRepository;
	
	@Autowired
	@InjectMocks
	private GameService gameService;
	
	@Mock
	private TeamService teamServiceMock;
	
	@Mock
	private WeekService weekService;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Before
	public void setup()
	{
		
	}

	
	/*@Test
	public void createGame_FAVTeamNull_GameValidationException()
	{
		
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.FAVORITE_IS_NULL.toString());
	    
	    
	    Team  team = new Team();
	    team.setId(UUID.randomUUID().toString());
	    
	    Game game = new Game();
	    
		when(teamServiceMock.getTeam(team.getId())).thenReturn(null);
			
	    gameService.createGame(game);
	}
	
	@Test
	public void createGame_DogTeamNull_GameValidationException()
	{
		
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.DOG_IS_NULL.toString());
	    Team  team = new Team();
	    team.setId(UUID.randomUUID().toString());
	    
	    Game game = new Game();
	    
		when(teamServiceMock.getTeam(team.getId())).thenReturn(null);
		game.setFavId(UUID.randomUUID().toString());
	    gameService.createGame(game);
		
	}
	
	@Test
	public void createGame_WeekTeamNull_GameValidationException()
	{
		
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.WEEK_IS_NULL.toString());
	    Team  team = new Team();
	    team.setId(UUID.randomUUID().toString());
	    
	    Game game = new Game();
	    
		when(teamServiceMock.getTeam(team.getId())).thenReturn(null);
		game.setFavId(UUID.randomUUID().toString());
		game.setDogId(UUID.randomUUID().toString());
	    gameService.createGame(game);
		
	}
	*/
	@Test 
	public void validate_GameNull_GameValidationException () {
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.GAME_IS_NULL.toString());
	    gameService.validateGame(null);
	}
	
	
	@Test 
	public void validate_FavNull_GameValidationException () {
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.FAVORITE_IS_NULL.toString());
	    
	    Game game=new Game();
	    gameService.validateGame(game);
	}
	
	@Test 
	public void validate_DogNull_GameValidationException () {
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.DOG_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    gameService.validateGame(game);
	}
	
	@Test 
	public void validate_WeekNull_GameValidationException () {
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.WEEK_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    gameService.validateGame(game);
	}
	
	@Test 
	public void validate_GameStartNull_GameValidationException () {
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.GAMESTART_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    gameService.validateGame(game);
	}
	
	@Test 
	public void validate_TeamCantPlay_GameValidationException () {
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.TEAM_CANNOT_PLAY_ITSELF.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(game.getFavId());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now() );
	    
	    gameService.validateGame(game);
	}
	
	@Test
	public void createGame_FavTeamNull_Exception()
	{
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.FAVORITE_TEAM_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    when(teamServiceMock.getTeam(game.getFavId())).thenReturn(null);
	    
	    gameService.createGame(game);
		
	}
	
	@Test
	public void createGame_DogTeamNull_Exception()
	{
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.DOG_TEAM_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    
	    Team  favTeam = new Team();
	    favTeam.setId(UUID.randomUUID().toString());
	    favTeam.setShortName(UUID.randomUUID().toString());
	    
	    
	    when(teamServiceMock.getTeam(game.getFavId())).thenReturn(favTeam);
	    when(teamServiceMock.getTeam(game.getDogId())).thenReturn(null);
	    
	    gameService.createGame(game);
		
	}
	
	@Test
	public void createGame_saveRepository()
	{
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    
	    Team  favTeam = new Team();
	    favTeam.setId(UUID.randomUUID().toString());
	    favTeam.setShortName(UUID.randomUUID().toString());
	    
	    Team  dogTeam = new Team();
	    dogTeam.setId(UUID.randomUUID().toString());
	    dogTeam.setShortName(UUID.randomUUID().toString());
	    
	    
	    when(teamServiceMock.getTeam(game.getFavId())).thenReturn(favTeam);
	    when(teamServiceMock.getTeam(game.getDogId())).thenReturn(dogTeam);
	    
	    gameService.createGame(game);
		
	    verify(gameRepository).save(game);
	}
	
	@Test
	public void updateGame_FindFromDsNull_Exception()
	{
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.UPDATE_GAME_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    game.setId(UUID.randomUUID().toString());
	    
	    when(gameRepository.findOne(game.getId())).thenReturn(null);
	    
	    gameService.updateGame(game);
		
	}
	
	@Test
	public void updateGame_saveRepository_pass()
	{
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    game.setId(UUID.randomUUID().toString());
	    
	    when(gameRepository.findOne(game.getId())).thenReturn(game);
	    
	    gameService.updateGame(game);
	    
	    verify(gameRepository).save(game);
	}
	
	@Test
	public void updateGameScore_gameNull_Exception()
	{
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.UPDATE_GAME_SCORE_IS_NULL.toString());
	    
	    gameService.updateGameScore(null);
	}
	
	@Test
	public void updateGameScore_findFromDsNull_Exception()
	{
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.UPDATE_GAME_SCORE_VALUE_IS_NULL.toString());
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    game.setId(UUID.randomUUID().toString());
	    
	    when(gameRepository.findOne(game.getId())).thenReturn(null);
	    
	    gameService.updateGameScore(game);
	    
	    verify(gameRepository).save(game);
	}
	
	@Test
	public void updateGameScore_findFromDsNotNull()
	{
	    
	    Game game=new Game();
	    game.setFavId(UUID.randomUUID().toString());
	    game.setDogId(UUID.randomUUID().toString());
	    game.setWeekId(UUID.randomUUID().toString());
	    game.setGameStart(ZonedDateTime.now());
	    game.setId(UUID.randomUUID().toString());
	    
	    when(gameRepository.findOne(game.getId())).thenReturn(game);
	    
	    when(gameRepository.save(game)).thenReturn(game);
	    
	    assertNotNull(gameService.updateGameScore(game));
	}
	
	@Test
	public void getGamesByWeek_gamesNull_Exception()
	{
		expectedEx.expect(GameValidationException.class);
	    expectedEx.expectMessage(GameExceptions.GET_GAMES_BY_WEEK_IS_NULL.toString());
	    
	   String weekId=UUID.randomUUID().toString();
	    
	    when(gameRepository.findByWeekId(weekId)).thenReturn(null);
	    
	    gameService.getGamesByWeek(weekId);
	}
	
	@Test
	public void getGamesByWeek_gamesNotNull_pass()
	{
	    
	   String weekId=UUID.randomUUID().toString();
	   
	   Game game1=new Game();
	    game1.setFavId(UUID.randomUUID().toString());
	    game1.setDogId(UUID.randomUUID().toString());
	    game1.setWeekId(weekId);
	    game1.setGameStart(ZonedDateTime.now());
	    game1.setId(UUID.randomUUID().toString());
	    
	    Game game2=new Game();
	    game2.setFavId(UUID.randomUUID().toString());
	    game2.setDogId(UUID.randomUUID().toString());
	    game2.setWeekId(weekId);
	    game2.setGameStart(ZonedDateTime.now());
	    game2.setId(UUID.randomUUID().toString());
	    
	    List<Game> games=new ArrayList<>();
	    games.add(game1);
	    games.add(game2);
	    
	    when(gameRepository.findByWeekId(weekId)).thenReturn(games);
	    
	   assertNotNull(gameService.getGamesByWeek(weekId));
	}
	
	/*@Test 
	public void createGame_favTeamNull_throwException () {
		Team  favTeam = new Team();
	    favTeam.setId(UUID.randomUUID().toString());
	    favTeam.setShortName(UUID.randomUUID().toString());
	    
	    
	    Team  dogTeam = new Team();
	    dogTeam.setId(UUID.randomUUID().toString());
	    dogTeam.setShortName(UUID.randomUUID().toString());
	    
	    
	    Game game = new Game();
	    game.setFavId(UUID.randomUUID().toString());
		game.setDogId(UUID.randomUUID().toString());
		game.setWeekId(UUID.randomUUID().toString());
		
		
		when(teamServiceMock.getTeam(game.getFavId())).thenReturn(favTeam);
		when(teamServiceMock.getTeam(game.getDogId())).thenReturn(dogTeam);
	    gameService.createGame(game);
	}
	*/
	/*@Test 
	public void createGame_FavTeamNull_throwException () {
		Team  favTeam = new Team();
	    favTeam.setId(UUID.randomUUID().toString());
	    favTeam.setShortName(UUID.randomUUID().toString());
	    
	    
	    Team  dogTeam = new Team();
	    dogTeam.setId(UUID.randomUUID().toString());
	    dogTeam.setShortName(UUID.randomUUID().toString());
	    
	    
	    Game game = new Game();
	    game.setFavId(UUID.randomUUID().toString());
		game.setDogId(UUID.randomUUID().toString());
		game.setWeekId(UUID.randomUUID().toString());
		
		
		when(teamServiceMock.getTeam(game.getFavId())).thenReturn(null);
		when(teamServiceMock.getTeam(game.getDogId())).thenReturn(dogTeam);
	    gameService.createGame(game);
	}
	*/
	/*@Test 
	public void validate () {
	    
	    Game game = new Game();
	    game.setId(UUID.randomUUID().toString());
//	    game.setDogId(UUID.randomUUID().toString());
	    
			
	    gameService.validateGame(null);
		
	}*/
	
//	public buildGame() {
//		
//	}
	
	
/*
	@Test
	public void testGetGameById() {
		Week week = createWeek("1", 1);
		Game game = createGame(week.getId(), "1", "2", 5.5, System.currentTimeMillis());
		
		assertEquals(game, gameServiceMock.getGameById(game.getId()));
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
		
		game = gameServiceMock.createGame(game);
		
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
		game = gameServiceMock.updateGame(game);
		
		game = gameServiceMock.getGameById(game.getId());
		assertEquals(35, game.getFavScore());
		assertEquals(21, game.getDogScore());
	}*/

	/*@Test
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
		
		List<Game> games = gameServiceMock.getGamesByWeek(week1Season1.getId());
		assertTrue(games.contains(game1Week1));
		assertTrue(games.contains(game2Week1));
		assertTrue(games.contains(game3Week1));
		assertFalse(games.contains(game1Week2));
		assertFalse(games.contains(game2Week2));
		assertFalse(games.contains(game1Week1Season2));
		assertFalse(games.contains(game2Week1Season2));
		assertFalse(games.contains(game3Week1Season2));
		
		games = gameServiceMock.getGamesByWeek(week2Season1.getId());
		assertTrue(games.contains(game1Week2));
		assertTrue(games.contains(game2Week2));
		assertFalse(games.contains(game1Week1));
		assertFalse(games.contains(game2Week1));
		assertFalse(games.contains(game3Week1));
		assertFalse(games.contains(game1Week1Season2));
		assertFalse(games.contains(game2Week1Season2));
		assertFalse(games.contains(game3Week1Season2));
		
		games = gameServiceMock.getGamesByWeek(week1Season2.getId());
		assertTrue(games.contains(game1Week1Season2));
		assertTrue(games.contains(game2Week1Season2));
		assertTrue(games.contains(game3Week1Season2));
		assertFalse(games.contains(game1Week1));
		assertFalse(games.contains(game2Week1));
		assertFalse(games.contains(game3Week1));
		assertFalse(games.contains(game1Week2));
		assertFalse(games.contains(game2Week2));
	}*/

}

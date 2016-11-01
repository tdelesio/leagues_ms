package com.makeurpicks.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.makeurpicks.PicksApplication;
import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.exception.PickValidationException;
import com.makeurpicks.exception.PickValidationException.PickExceptions;
import com.makeurpicks.game.GameIntegrationService;
import com.makeurpicks.game.GameResponse;
import com.makeurpicks.repository.DoublePickRepository;
import com.makeurpicks.repository.PickRepository;
import com.makeurpicks.repository.PicksByWeekRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = PicksApplication.class)
public class PickServiceTest {

	@Autowired
	@InjectMocks
	private PickService service;
	
	@Mock
	private GameIntegrationService gameIntegrationMock;
	
	@Mock
	private PickRepository pickRepositoryMock;

	@Mock
	private DoublePickRepository doublePickRepositoryMock;
	
	@Mock
	private PicksByWeekRepository picksByWeekRepositoryMock;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Test
	public void makePick_ValidateIdIsSet() {
		Pick pick = new Pick();
		pick.setId(null);
		pick = service.makePick(pick);
		assertTrue(pick.getId() != null);
		assertTrue(pick.getId() instanceof String);

	}
	
	@Test
	public void validatePicks_GameIdIsNotNull()
	{
		Pick pick = new Pick();
		try
		{
			service.validatePick(pick, false);
			fail("Game ID is null");
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.hasSpecificException(PickExceptions.GAME_IS_NULL));
		}
	}
	
	@Test
	public void validatePicks_TeamIdIsNotNull()
	{
		Pick pick = new Pick();
		pick.setGameId(UUID.randomUUID().toString());
		try
		{
			service.validatePick(pick, false);
			fail("Team ID is null");
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.hasSpecificException(PickExceptions.TEAM_IS_NULL));
		}
	}
	
	@Test
	public void validatePicks_WeekIdIsNotNull()
	{
		Pick pick = new Pick();
		pick.setGameId(UUID.randomUUID().toString());
		pick.setTeamId(UUID.randomUUID().toString());
		try
		{
			service.validatePick(pick, false);
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.hasSpecificException(PickExceptions.WEEK_IS_NULL));
		}
	}
	
	@Test
	public void validatePicks_LeagueIdIsNotNull()
	{
		Pick pick = new Pick();
		pick.setGameId(UUID.randomUUID().toString());
		pick.setTeamId(UUID.randomUUID().toString());
		pick.setWeekId(UUID.randomUUID().toString());
		try
		{
			service.validatePick(pick, false);
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.hasSpecificException(PickExceptions.LEAGUE_IS_NULL));
		}
	}
	
	@Test
	public void validatePicks_PlayerIdIsNotNull()
	{
		Pick pick = new Pick();
		pick.setGameId(UUID.randomUUID().toString());
		pick.setTeamId(UUID.randomUUID().toString());
		pick.setWeekId(UUID.randomUUID().toString());
		pick.setLeagueId(UUID.randomUUID().toString());
		try
		{
			service.validatePick(pick, false);
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.hasSpecificException(PickExceptions.PLAYER_IS_NUll));
		}
	}
	
	@Test
	public void validatePicks_GameIDIsValidGame()
	{
		Pick pick = new Pick();
		String gameId = UUID.randomUUID().toString();
		pick.setGameId(gameId);
		pick.setTeamId(UUID.randomUUID().toString());
		pick.setWeekId(UUID.randomUUID().toString());
		pick.setLeagueId(UUID.randomUUID().toString());
		pick.setPlayerId(UUID.randomUUID().toString());
		
		when(gameIntegrationMock.getGameById(gameId)).thenReturn(null);
		try
		{
			service.validatePick(pick, false);
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.hasSpecificException(PickExceptions.GAME_IS_NULL));
		}
	}
	
	@Test
	public void validatePicks_TeamsPassedArePlayingEachOther()
	{
		Pick pick = new Pick();
		String gameId = UUID.randomUUID().toString();
		pick.setGameId(gameId);
		String teamId = UUID.randomUUID().toString();
		pick.setTeamId(teamId);
		pick.setWeekId(UUID.randomUUID().toString());
		pick.setLeagueId(UUID.randomUUID().toString());
		pick.setPlayerId(UUID.randomUUID().toString());
		
		GameResponse gameResponse = new GameResponse();
		gameResponse.setId(gameId);
		gameResponse.setFavId(UUID.randomUUID().toString());
		gameResponse.setDogId(UUID.randomUUID().toString());
		gameResponse.setGameStart(ZonedDateTime.now().plusDays(1));
		when(gameIntegrationMock.getGameById(gameId)).thenReturn(gameResponse);
		try
		{
			service.validatePick(pick, false);
			fail();
		}
		catch (PickValidationException exception)
		{
//			System.out.println(exception);
			assertTrue(exception.hasSpecificException(PickExceptions.TEAM_NOT_PLAYING_IN_GAME));
		}
	}
	
	@Test
	public void validatePicks_GameHasNotStartedNoPicksAllowed()
	{
		Pick pick = new Pick();
		String gameId = UUID.randomUUID().toString();
		pick.setGameId(gameId);
		String teamId = UUID.randomUUID().toString();
		pick.setTeamId(teamId);
		pick.setWeekId(UUID.randomUUID().toString());
		pick.setLeagueId(UUID.randomUUID().toString());
		pick.setPlayerId(UUID.randomUUID().toString());
		
		GameResponse gameResponse = new GameResponse();
		gameResponse.setId(gameId);
		gameResponse.setFavId(UUID.randomUUID().toString());
		gameResponse.setDogId(UUID.randomUUID().toString());
		gameResponse.setGameStart(ZonedDateTime.now().minusDays(1));
		when(gameIntegrationMock.getGameById(gameId)).thenReturn(gameResponse);
		
		try
		{
			service.validatePick(pick, false);
			fail();
		}
		catch (PickValidationException exception)
		{
//			System.out.println(exception);
			assertTrue(exception.hasSpecificException(PickExceptions.GAME_HAS_ALREADY_STARTED));
		}
	}
		
	
	@Test
	public void updatePick_pickNotFound_PickValidationExceptionThrown() {
		
		expectedEx.expect(RuntimeException.class);
	    expectedEx.expectMessage(PickExceptions.PICK_IS_NULL.toString());
	    
		Pick pick = new Pick();
		String pickId = UUID.randomUUID().toString();
		pick.setId(pickId);
		String gameId = UUID.randomUUID().toString();
		pick.setGameId(gameId);
		String teamId = UUID.randomUUID().toString();
		pick.setTeamId(teamId);
		pick.setWeekId(UUID.randomUUID().toString());
		pick.setLeagueId(UUID.randomUUID().toString());
		pick.setPlayerId(UUID.randomUUID().toString());

		GameResponse gameResponse = new GameResponse();
		gameResponse.setId(gameId);
		gameResponse.setFavId(teamId);
		gameResponse.setDogId(UUID.randomUUID().toString());
		gameResponse.setGameStart(ZonedDateTime.now().plusDays(3));
		
		when(gameIntegrationMock.getGameById(gameId)).thenReturn(gameResponse);
		when(pickRepositoryMock.findOne(pickId)).thenReturn(null);
		
		service.updatePick(pick);
	}
	
	@Test()
	public void updatePick_gameIsADouble_deleteDouble() {
	    
		Pick pick = new Pick();
		
		String playerId = UUID.randomUUID().toString();
		String pickId = UUID.randomUUID().toString();
		String gameId = UUID.randomUUID().toString();
		String teamId = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		
		pick.setPlayerId(playerId);
		pick.setId(pickId);
		pick.setGameId(gameId);
		pick.setTeamId(teamId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);

		Pick pickFromDS = new Pick();
		pickFromDS.setPlayerId(playerId);
		
		GameResponse gameResponse = new GameResponse();
		gameResponse.setId(gameId);
		gameResponse.setFavId(teamId);
		gameResponse.setDogId(UUID.randomUUID().toString());
		gameResponse.setGameStart(ZonedDateTime.now().plusDays(1));
		
		DoublePick doublePick = new DoublePick();
		doublePick.setPickId(pickId);
		
		when(gameIntegrationMock.getGameById(gameId)).thenReturn(gameResponse);		
		when(pickRepositoryMock.findOne(pickId)).thenReturn(pickFromDS);
		when(doublePickRepositoryMock.findDoubleForPlayer(leagueId, weekId, playerId)).thenReturn(doublePick);
		
		service.updatePick(pick);
		
		verify(doublePickRepositoryMock).delete(doublePick);
	}
	
	@Test()
	public void updatePick_saveGame_pass() {
		
		String playerId = UUID.randomUUID().toString();
		String pickId = UUID.randomUUID().toString();
		String gameId = UUID.randomUUID().toString();
		String teamId = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		
		Pick pick = new Pick();
		pick.setPlayerId(playerId);
		pick.setId(pickId);
		pick.setGameId(gameId);
		pick.setTeamId(teamId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);

		Pick pickFromDS = new Pick();
		pickFromDS.setPlayerId(playerId);
		
		GameResponse gameResponse = new GameResponse();
		gameResponse.setId(gameId);
		gameResponse.setFavId(teamId);
		gameResponse.setDogId(UUID.randomUUID().toString());
		gameResponse.setGameStart(ZonedDateTime.now().plusDays(1));
		
		DoublePick doublePick = new DoublePick();
		doublePick.setPickId(pickId);
		
		when(gameIntegrationMock.getGameById(gameId)).thenReturn(gameResponse);		
		when(pickRepositoryMock.findOne(pickId)).thenReturn(pickFromDS);
		
		service.updatePick(pick);
		
		verify(pickRepositoryMock).save(pick);
	}
	
	
	@Test
	public void getPicksByWeekAndPlayerTest_mapFromDSNull_returnEmptyMap() {
		
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		Pick pick = new Pick();
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(null);
		
		Map<String, Pick> map = service.getPicksByWeekAndPlayer(leagueId, weekId, playerId);
		
		assertTrue(map.size() == 0);
	}
	
	// **************************
	// Double check if this needs another test for nulls
	// **************************
	
	@Test
	public void getPicksByWeekAndPlayerTest_noPicksByPlayer_returnEmptyMap() {
		
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		String gameId = UUID.randomUUID().toString();
		String pickId = UUID.randomUUID().toString();
		String secondPlayerId = UUID.randomUUID().toString();
		Pick pick = new Pick();
		pick.setId(pickId);
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		Map<String, Map<String, String>> playersByWeekMap = new HashMap<>();
		Map<String, String> gamesMap = new HashMap<>();
		Map<String, Pick> pickMap = new HashMap<>();
		
		gamesMap.put(gameId, pickId);
		
		playersByWeekMap.put(secondPlayerId, gamesMap);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(playersByWeekMap);
		when(pickRepositoryMock.findOne(pickId)).thenReturn(pick);
		
		Map<String, Pick> map = service.getPicksByWeekAndPlayer(leagueId, weekId, playerId);
		
		assertTrue(map.size() == 0);
	}
	
	@Test
	public void getPicksByWeekAndPlayerTest_foundPicks_pass() {
		
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		String gameId = UUID.randomUUID().toString();
		String pickId = UUID.randomUUID().toString();
		Pick pick = new Pick();
		pick.setId(pickId);
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		Map<String, Map<String, String>> playersByWeekMap = new HashMap<>();
		Map<String, String> gamesMap = new HashMap<>();
		Map<String, Pick> pickMap = new HashMap<>();
		
		gamesMap.put(gameId, pickId);
		
		playersByWeekMap.put(playerId, gamesMap);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(playersByWeekMap);
		when(pickRepositoryMock.findOne(pickId)).thenReturn(pick);
		
		Map<String, Pick> map = service.getPicksByWeekAndPlayer(leagueId, weekId, playerId);
		
		assertEquals(map.get(gameId).getId(), pickId);
	}
	
	

//	@Test
//	public void test() {
//		fail("Not yet implemented");
//	}
	@Test
	public void getOtherPicksByWeekAndPlayerTest_mapFromDSisNull_returnEmptyMap() {
		
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		Pick pick = new Pick();
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(null);
		
		Map<String, Pick> map = service.getOtherPicksByWeekAndPlayer(leagueId, weekId, playerId);
		
		assertTrue(map.size() == 0);
	}

	@Test
	public void getOtherPicksByWeekAndPlayerTest_foundPicks_pass() {
		
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		String gameId = UUID.randomUUID().toString();
		String pickId = UUID.randomUUID().toString();
		Pick pick = new Pick();
		pick.setId(pickId);
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		Map<String, Map<String, String>> playersByWeekMap = new HashMap<>();
		Map<String, String> gamesMap = new HashMap<>();
		Map<String, Pick> pickMap = new HashMap<>();
		
		gamesMap.put(gameId, pickId);
		
		playersByWeekMap.put(playerId, gamesMap);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(playersByWeekMap);
		when(pickRepositoryMock.findOne(pickId)).thenReturn(pick);
		
		Map<String, Pick> map = service.getOtherPicksByWeekAndPlayer(leagueId, weekId, playerId);
		
		assertEquals(map.get(gameId).getId(), pickId);
	}

	
	@Test
	public void testGetPicksByWeek_mapFromDSisNull_returnEmptyMap() {
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		Pick pick = new Pick();
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(null);
		
		Map<String, Map<String, Pick>> map = service.getPicksByWeek(leagueId, weekId);
		
		assertTrue(map.size() == 0);
	}

	@Test
	public void testGetPicksByWeek() {
		String leagueId = UUID.randomUUID().toString();
		String weekId = UUID.randomUUID().toString();
		String playerId = UUID.randomUUID().toString();
		
		Pick pick = new Pick();
		pick.setPlayerId(playerId);
		pick.setWeekId(weekId);
		pick.setLeagueId(leagueId);
		
		when(picksByWeekRepositoryMock.getPlayersByWeek(leagueId, weekId)).thenReturn(null);
		
		Map<String, Map<String, Pick>> map = service.getPicksByWeek(leagueId, weekId);
		
		assertTrue(map.size() == 0);
	}
//	@Test
//	public void testGetDoublePick() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetDoublePicks() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testMakeDoublePick() {
//		fail("Not yet implemented");
//	}

}
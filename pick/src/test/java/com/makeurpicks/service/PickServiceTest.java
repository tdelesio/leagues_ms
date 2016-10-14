package com.makeurpicks.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.PicksApplication;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.exception.PickValidationException;
import com.makeurpicks.exception.PickValidationException.PickExceptions;
import com.makeurpicks.game.GameIntegrationService;
import com.makeurpicks.game.GameResponse;

import junit.framework.Assert;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = PicksApplication.class)
public class PickServiceTest {

	@Autowired
	@InjectMocks
	private PickService service;
	
	@Mock
	private GameIntegrationService gameIntegrationMock;
	
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
			System.out.println(exception);
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
			System.out.println(exception);
			assertTrue(exception.hasSpecificException(PickExceptions.GAME_HAS_ALREADY_STARTED));
		}
	}
	
	
	
	

//	@Test
//	public void testUpdatePick() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPicksByWeekAndPlayer() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetOtherPicksByWeekAndPlayer() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testGetPicksByWeek() {
//		fail("Not yet implemented");
//	}
//
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

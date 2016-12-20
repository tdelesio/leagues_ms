package com.makeurpicks.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueBuilder;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.PlayerLeague;
import com.makeurpicks.domain.PlayerLeagueId;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.exception.LeagueValidationException.LeagueExceptions;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.PlayerLeagueRepository;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class LeagueServiceTest {
	 
	@Mock
	public LeagueRepository leagueRepositoryMock;
	@Mock
	public PlayerLeagueRepository playerLeagueRepository;
	@Autowired 
	@InjectMocks
	public LeagueService leagueService;

	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@AfterClass
	public static void close() {
	}
	
	@Test
	public void validateLeague_leagueNameEmpty_throwsLeagueValidationException() {
		
		expectedEx.expect(RuntimeException.class);
	    expectedEx.expectMessage(LeagueExceptions.LEAGUE_NAME_IS_NULL.toString());
	    
		String leagueId = UUID.randomUUID().toString();
		
		League league = new LeagueBuilder(leagueId)
			.build();
		
		leagueService.validateLeague(league);
		
	}
	
	@Test
	public void validateLeague_duplicateLeague_throwsLeagueValidationException() {
		
		expectedEx.expect(RuntimeException.class);
	    expectedEx.expectMessage(LeagueExceptions.LEAGUE_NAME_IN_USE.toString());
	    
	    String leagueId = UUID.randomUUID().toString();
		
		League league = new LeagueBuilder(leagueId)
			.withName("pickem")
			.build();
		when(leagueService.getLeagueByName(league.getLeagueName())).thenReturn(league);
		leagueService.validateLeague(league);
		
	}
	
	@Test
	public void validateLeague_sessionIdNull_throwsLeagueValidationException() {
		
		expectedEx.expect(RuntimeException.class);
	    expectedEx.expectMessage(LeagueExceptions.SEASON_ID_IS_NULL.toString());
	    
	    String leagueId = UUID.randomUUID().toString();
		League league = new LeagueBuilder(leagueId)
			.withName("pickem")
			.build();
		
		when(leagueService.getLeagueByName(league.getLeagueName())).thenReturn(null);
		
		leagueService.validateLeague(league);
		
	}
	
	@Test
	public void validateLeague_adminNotFound_throwsLeagueValidationException() {
		expectedEx.expect(LeagueValidationException.class);
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		
		League league = new LeagueBuilder(leagueId)
			.withName("pickem")
			.withSeasonId(seasonId)
			.build();
		when(leagueService.getLeagueByName(league.getLeagueName())).thenReturn(null);
		leagueService.validateLeague(league);
		
	}
	
	@Test
	public void validateLeague_adminNotFound_2_throwsLeagueValidationException() {
		expectedEx.expect(RuntimeException.class);
	    expectedEx.expectMessage(LeagueExceptions.ADMIN_NOT_FOUND.toString());
	    String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		
		League league = new LeagueBuilder(leagueId)
			.withName("pickem")
			.withSeasonId(seasonId)
			.build();
		when(leagueService.getLeagueByName(league.getLeagueName())).thenReturn(null);
		leagueService.validateLeague(league);
		
	}
	
	@Test
	public void createLeagueShouldCallSaveOnLeagueRepository() {
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		League league = new LeagueBuilder(leagueId)
				.withAdminId(player1Id)
				.withName("pickem")
				.withPassword("football")
				.withSeasonId(seasonId).withNoSpreads()
				.build();
		leagueService.createLeague(league);
		verify(leagueRepositoryMock).save(league);
	}
	
	@Test
	public void createLeagueShouldAddaPlayerLeague() {
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		League league = new LeagueBuilder(leagueId)
				.withAdminId(player1Id)
				.withName("pickem")
				.withPassword("football")
				.withSeasonId(seasonId).withNoSpreads()
				.build();
		when(leagueRepositoryMock.findOne(leagueId)).thenReturn(league);
		/*PlayerLeague playerLeague = new PlayerLeague();
		playerLeague.setLeagueId(league.getId());
		playerLeague.setLeagueName(league.getLeagueName());
		playerLeague.setPassword(league.getPassword());
		playerLeague.setPlayerId(player1Id);*/
//		when(leagueService.addPlayerToLeague(league, player1Id)).thenReturn(playerLeague);
		leagueService.createLeague(league);
		
	}
	

	@Test
	public void updateLeagueShouldCallSaveOnLeagueRepository() {
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		League league = new LeagueBuilder(leagueId)
				.withAdminId(player1Id)
				.withName("pickem")
				.withPassword("football")
				.withSeasonId(seasonId).withNoSpreads()
				.build();
		when(leagueRepositoryMock.findOne(leagueId)).thenReturn(league);
		leagueService.updateLeague(league);
		verify(leagueRepositoryMock).save(league);
	}
	
	@Test
	public void getLeaguesForPlayerNoleaguesForGivenPlayerGivesEmptyLeagueSet() {
		String playerId = UUID.randomUUID().toString();
		when(playerLeagueRepository.findIdLeagueIdsByIdPlayerId(playerId)).thenReturn(null);
		Set<LeagueName> set = leagueService.getLeaguesForPlayer(playerId);
		assertTrue(set.size() == 0);
	}
	
	@Test
	public void updateLeagueOnNonExistingLeagueThrowsLeagueValidationExceptionLeagueNotFound() {
		expectedEx.expect(LeagueValidationException.class);
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		League league = new LeagueBuilder(leagueId)
				.withAdminId(player1Id)
				.withName("pickem")
				.withPassword("football")
				.withSeasonId(seasonId).withNoSpreads()
				.build();
		when(leagueRepositoryMock.findOne(leagueId)).thenReturn(null);
		leagueService.updateLeague(league);
	}
	
	@Test
	public void removePlayerFromLeagueOnInvalidLeagueThrowsLeagueValidationLeagueNotFound() {
		expectedEx.expect(LeagueValidationException.class);
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		when(leagueRepositoryMock.findOne(leagueId)).thenReturn(null);
		leagueService.removePlayerFromLeague(leagueId, player1Id);
	}
	
	@Test
	public void removePlayerFromLeagueOnExecutionShouldCalldeleteOnPlayerLeaguerepository() {
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		League league = new LeagueBuilder(leagueId)
				.withAdminId(player1Id)
				.withName("pickem")
				.withPassword("football")
				.withSeasonId(seasonId).withNoSpreads()
				.build();
		PlayerLeague playerLeague = new PlayerLeague(new PlayerLeagueId(league.getId(),player1Id));
		playerLeague.setLeagueId(league.getId());
		playerLeague.setLeagueName(league.getLeagueName());
		playerLeague.setPassword(league.getPassword());
		playerLeague.setPlayerId(player1Id);
		when(leagueRepositoryMock.findOne(leagueId)).thenReturn(league);
		when(playerLeagueRepository.findByIdLeagueIdAndIdPlayerId(league.getId(),player1Id)).thenReturn(playerLeague);
		leagueService.removePlayerFromLeague(leagueId, player1Id);
		verify(playerLeagueRepository).delete(playerLeague);
	}

}

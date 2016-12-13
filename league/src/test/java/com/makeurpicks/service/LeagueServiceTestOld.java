package com.makeurpicks.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.makeurpicks.LeagueApplication;
import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueBuilder;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.LeaguesPlayerJoined;
import com.makeurpicks.domain.PlayersInLeague;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.LeaguesAPlayHasJoinedRespository;
import com.makeurpicks.repository.PlayersInLeagueRepository;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = LeagueApplication.class)
public class LeagueServiceTestOld {
	 
	@Autowired 
	@InjectMocks
	private LeagueService leagueService;
	
	@Mock
	private LeagueRepository leagueRepositoryMock;
	
	@Mock
	private LeaguesAPlayHasJoinedRespository leaguesAPlayerHasJoinedRespositoryMock;
	
	@Mock
	private PlayersInLeagueRepository playersInLeagueRepositoryMock;
	
	
//	@Before
//	public void setup()
//	{
//		MockitoAnnotations.initMocks(this);
//	}
	
//	@AfterClass
//	public static void close() {
//		System.clearProperty("config.port");
//		if (server != null) {
//			server.close();
//		}
//	}
	
	@Test
	public void testCreateLeague() {
		
		String player1Id = UUID.randomUUID().toString();
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
//		createPlayerMock(player1Id);
		
		
		League league = new LeagueBuilder(leagueId)
			.withAdminId(player1Id)
			.withName("pickem")
			.withPassword("football")
			.withSeasonId(seasonId)
			.build();
		
		
		//when(playersInLeagueRepositoryMock.addPlayerToLeague(player1Id, leagueId));
		
		createLeague(league);	

	}
	

	
	private League createLeague(League league)
	{
		when(leagueRepositoryMock.findAll()).thenReturn(Collections.emptyList());
		when(leagueRepositoryMock.save(league)).thenReturn(league);

		
		try {
			league = leagueService.createLeague(league);
			when(leagueRepositoryMock.findOne(league.getId())).thenReturn(league);
			
			//check to make sure league is created
			League leagueFromDS = leagueService.getLeagueById(league.getId());
			assertEquals(league.getId(), leagueFromDS.getId());
			
			LeaguesPlayerJoined leaguesPlayerJoined = new LeaguesPlayerJoined();
			LeagueName leagueName = new LeagueName();
			leagueName.setLeagueId(league.getId());
			leagueName.setLeagueName(league.getLeagueName());
			leagueName.setSeasonId(league.getSeasonId());
			Set<LeagueName> leagueNames = new HashSet<>();
			leagueNames.add(leagueName);
			leaguesPlayerJoined.setLeauges(leagueNames);
			
			when(leaguesAPlayerHasJoinedRespositoryMock.findOne(league.getAdminId())).thenReturn(leaguesPlayerJoined);
			
			PlayersInLeague pil = new PlayersInLeague();
			Set<String> p = new HashSet<>();
			p.add(league.getAdminId());
			pil.setPlayers(p);
			when(playersInLeagueRepositoryMock.findOne(league.getId())).thenReturn(pil);
			
			//check to see that player is part of league
			Set<LeagueName> leagueNamesFromService = leagueService.getLeaguesForPlayer(league.getAdminId());
			LeagueName leagueNameFromService = new LeagueName(league);
			assertTrue(leagueNamesFromService.contains(leagueNameFromService));
			
			//check to see that league has the player in it
			Set<String> Strings = leagueService.getPlayersInLeague(league.getId());
			assertTrue(Strings.contains(league.getAdminId()));
			
		} catch (LeagueValidationException e) {
			fail(e.getMessage());
		}
		
		return league;
	}
//
////	@Test
////	public void testUpdateLeague() {
////		fail("Not yet implemented");
////	}
////
	@Test
	public void testGetLeaguesForPlayer() {
		String player1Id = "1";
		String player2Id = "2";
		String player3Id = "3";
		String player4Id = "4";
		String player5Id = "5";
		
//		createPlayerMock(player1Id);
//		createPlayerMock(player2Id);
//		createPlayerMock(player3Id);
//		createPlayerMock(player4Id);
//		createPlayerMock(player5Id);
		
		League league1 = new LeagueBuilder()
		.withAdminId(player1Id)
		.withName("pickem")
		.withPassword("football")
		.withSeasonId("1")
		.build();
		
		League league2 = new LeagueBuilder()
		.withAdminId(player1Id)
		.withName("suicide")
		.withPassword("football")
		.withSeasonId("1")
		.build();
		
		League league3 = new LeagueBuilder()
		.withAdminId(player1Id)
		.withName("superbowl")
		.withPassword("football")
		.withSeasonId("1")
		.build();
	
		league1 = createLeague(league1);
		league2 = createLeague(league2);
		league3 = createLeague(league3);
		
		//player 2 will join both leagues
		leagueService.addPlayerToLeague(league1, player2Id); 
		leagueService.addPlayerToLeague(league2, player2Id);
		
		//player 3 will join first league
		leagueService.addPlayerToLeague(league1, player3Id);
		
		//player 4 will join second league
		leagueService.addPlayerToLeague(league2, player4Id);
		
		LeagueName league1Name = new LeagueName();
		league1Name.setLeagueId(league1.getId());
		league1Name.setLeagueName(league1.getLeagueName());
		league1Name.setSeasonId(league1.getSeasonId());
		
		LeagueName league2Name = new LeagueName();
		league2Name.setLeagueId(league2.getId());
		league2Name.setLeagueName(league2.getLeagueName());
		league2Name.setSeasonId(league2.getSeasonId());
		
		LeagueName league3Name = new LeagueName();
		league3Name.setLeagueId(league3.getId());
		league3Name.setLeagueName(league3.getLeagueName());
		league3Name.setSeasonId(league3.getSeasonId());
		
		LeaguesPlayerJoined leaguesPlayer1Joined = new LeaguesPlayerJoined();
		LeaguesPlayerJoined leaguesPlayer2Joined = new LeaguesPlayerJoined();
		LeaguesPlayerJoined leaguesPlayer3Joined = new LeaguesPlayerJoined();
		LeaguesPlayerJoined leaguesPlayer4Joined = new LeaguesPlayerJoined();
		LeaguesPlayerJoined leaguesPlayer5Joined = new LeaguesPlayerJoined();
		
		Set<LeagueName> league1Names = new HashSet<>();
		league1Names.add(league1Name);
		league1Names.add(league2Name);
		league1Names.add(league3Name);
		
		leaguesPlayer1Joined.setLeauges(league1Names);
		
		when(leaguesAPlayerHasJoinedRespositoryMock.findOne(player1Id)).thenReturn(leaguesPlayer1Joined);
	
		
		//setup response for player 2
		Set<LeagueName> league2Names = new HashSet<>();
		league2Names.add(league1Name);
		league2Names.add(league2Name);
		
		leaguesPlayer2Joined.setLeauges(league2Names);
		
		when(leaguesAPlayerHasJoinedRespositoryMock.findOne(player2Id)).thenReturn(leaguesPlayer2Joined);
		
		//setup response for player 3
		Set<LeagueName> league3Names = new HashSet<>();
		league3Names.add(league1Name);
		
		leaguesPlayer3Joined.setLeauges(league3Names);
		
		when(leaguesAPlayerHasJoinedRespositoryMock.findOne(player3Id)).thenReturn(leaguesPlayer3Joined);
		
		//setup response for player4
		Set<LeagueName> league4Names = new HashSet<>();
		league4Names.add(league2Name);
		
		leaguesPlayer4Joined.setLeauges(league4Names);
		
		when(leaguesAPlayerHasJoinedRespositoryMock.findOne(player4Id)).thenReturn(leaguesPlayer4Joined);
		
		//setup blank response for player5
		when(leaguesAPlayerHasJoinedRespositoryMock.findOne(player5Id)).thenReturn(leaguesPlayer5Joined);
		
		//setup response for league1
		PlayersInLeague pil1 = new PlayersInLeague();
		Set<String> p1 = new HashSet<>();
		p1.add(player1Id);
		p1.add(player2Id);
		p1.add(player3Id);
		pil1.setPlayers(p1);
		
		when(playersInLeagueRepositoryMock.findOne(league1.getId())).thenReturn(pil1);
		
		//setup response for league2
		PlayersInLeague pil2 = new PlayersInLeague();
		Set<String> p2 = new HashSet<>(); 
		p2.add(player1Id);
		p2.add(player2Id);
		p2.add(player4Id);
		pil2.setPlayers(p2);
		
		when(playersInLeagueRepositoryMock.findOne(league2.getId())).thenReturn(pil2);
		
		//setup response for league3
		PlayersInLeague pil3 = new PlayersInLeague();
		Set<String> p3 = new HashSet<>();
		p3.add(player1Id);
		pil3.setPlayers(p3);
			
		when(playersInLeagueRepositoryMock.findOne(league2.getId())).thenReturn(pil2);
		
		Set<String> list;
		
		//player 5 won't join any leagues
		
		//league1 should have 3 players, 1, 2, 3
//		Set<String> Strings = playersInLeagueRepository.findOne(league1.getId()).getPlayers();
		Set<String> Strings = leagueService.getPlayersInLeague(league1.getId());
		
		assertTrue(Strings.contains(new String(player1Id)));
		assertTrue(Strings.contains(new String(player2Id)));
		assertTrue(Strings.contains(new String(player3Id)));
		assertEquals(3, Strings.size());
		
		//league2 should have 3 players, 1, 2, 4
//		Strings = playersInLeagueRepository.findOne(league2.getId()).getPlayers();
		Strings = leagueService.getPlayersInLeague(league2.getId());
		assertTrue(Strings.contains(new String(player1Id)));
		assertTrue(Strings.contains(new String(player2Id)));
		assertTrue(Strings.contains(new String(player4Id)));
		assertEquals(3, Strings.size());
		
		//league3 should have 1 player, 1
//		Strings = playersInLeagueRepository.findOne(league3.getId()).getPlayers();
		
		//player 1 should be part of leagues 1,2,3
//		Set<LeagueName> leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player1Id).getLeauges();
		Set<LeagueName> leagueNames = leagueService.getLeaguesForPlayer(player1Id);
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertTrue(leagueNames.contains(new LeagueName(league3)));
		assertEquals(3, leagueNames.size());
		
		//player 2 should be part of leagues 1,2
//		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player2Id).getLeauges();
		leagueNames = leagueService.getLeaguesForPlayer(player2Id);
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertEquals(2, leagueNames.size());
		
		//player 3 should be part of leagues 1
//		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player3Id).getLeauges();
		leagueNames = leagueService.getLeaguesForPlayer(player3Id);
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertEquals(1, leagueNames.size());
		
		//player 4 should be part of leagues 2
//		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player4Id).getLeauges();
		leagueNames = leagueService.getLeaguesForPlayer(player4Id);
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertEquals(1, leagueNames.size());
		
		 //player 5 should not be part of any leagues
//		assertTrue(leaguesAPlayerHasJoinedRespository.findOne(player5Id) == null);
		assertTrue(leagueService.getLeaguesForPlayer(player5Id)==null);
				

		
	}


}

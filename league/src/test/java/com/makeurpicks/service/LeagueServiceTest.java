package com.makeurpicks.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.makeurpicks.LeagueApplication;
import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueBuilder;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.PlayerResponse;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.LeaguesAPlayHasJoinedRespository;
import com.makeurpicks.repository.PlayersInLeagueRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LeagueApplication.class)
@IntegrationTest({ "server.port:0", "spring.cloud.config.enabled:true" })
//eureka.client.enabled=false",
@WebAppConfiguration
public class LeagueServiceTest {
	 
	@Autowired 
	@InjectMocks
	private LeagueService leagueService;
	
	@Autowired
	private LeagueRepository leagueRepository;
	
	@Autowired
	private LeaguesAPlayHasJoinedRespository leaguesAPlayerHasJoinedRespository;
	
	@Autowired
	private PlayersInLeagueRepository playersInLeagueRepository;
	
	@Mock
	private PlayerClient playerClientMock;
	
	private static ConfigurableApplicationContext server;
	
	private static int configPort = 8888;
	
	@BeforeClass
	public static void init() throws Exception {
	
//		String baseDir = ConfigServerTestUtils
//				.getBaseDirectory("league");
//		
////		String repo = ConfigServerTestUtils.prepareLocalRepo(baseDir, "target/repos",
////				"config-repo", "target/config");
//		String repo = "https://github.com/tdelesio/config-repo.git";
//		
//		server = SpringApplication.run(
//				org.springframework.cloud.config.server.ConfigServerApplication.class,
//				"--server.port=" + configPort, "--spring.config.name=server",
//				"--spring.cloud.config.server.git.uri=" + repo);
//		configPort = ((EmbeddedWebApplicationContext) server)
//				.getEmbeddedServletContainer().getPort();
//		System.setProperty("config.port", "" + configPort);
	}
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
		
		leagueService.setPlayerClient(playerClientMock);
		
		leagueRepository.deleteAll();
		
		leaguesAPlayerHasJoinedRespository.deleteAll();
		
		playersInLeagueRepository.deleteAll();
	}
	
	@AfterClass
	public static void close() {
		System.clearProperty("config.port");
		if (server != null) {
			server.close();
		}
	}
	
	@Test
	public void testCreateLeague() {
		
		String player1Id = "1";
		
		createPlayerMock(player1Id);
		
		
		League league = new LeagueBuilder()
			.withAdminId(player1Id)
			.withName("pickem")
			.withPassword("football")
			.withSeasonId("1")
			.build();
		
		createLeague(league);	
		
		
	}
	
	private void createPlayerMock(String playerId)
	{
		when(playerClientMock.getPlayerById(playerId)).thenReturn(new PlayerResponse(playerId));
	}
	
	private League createLeague(League league)
	{
		try {
			league = leagueService.createLeague(league);
			
			//check to make sure league is created
			assertEquals(league.getId(), leagueRepository.findOne(league.getId()).getId());
			
			//check to see that player is part of league
			Set<LeagueName> leagueNames = leaguesAPlayerHasJoinedRespository.findOne(league.getAdminId()).getLeauges();
			LeagueName leagueName = new LeagueName(league);
			assertTrue(leagueNames.contains(leagueName));
			
			//check to see that league has the player in it
			Set<PlayerResponse> playerResponses = playersInLeagueRepository.findOne(league.getId()).getPlayers();
			assertTrue(playerResponses.contains(new PlayerResponse(league.getAdminId())));
			
		} catch (LeagueValidationException e) {
			fail(e.getMessage());
		}
		
		return league;
	}

//	@Test
//	public void testUpdateLeague() {
//		fail("Not yet implemented");
//	}
//
	@Test
	public void testGetLeaguesForPlayer() {
		String player1Id = "1";
		String player2Id = "2";
		String player3Id = "3";
		String player4Id = "4";
		String player5Id = "5";
		
		createPlayerMock(player1Id);
		createPlayerMock(player2Id);
		createPlayerMock(player3Id);
		createPlayerMock(player4Id);
		createPlayerMock(player5Id);
		
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
		
		Set<String> list;
		
		//player 5 won't join any leagues
		
		//league1 should have 3 players, 1, 2, 3
		Set<PlayerResponse> playerResponses = playersInLeagueRepository.findOne(league1.getId()).getPlayers();
		assertTrue(playerResponses.contains(new PlayerResponse(player1Id)));
		assertTrue(playerResponses.contains(new PlayerResponse(player2Id)));
		assertTrue(playerResponses.contains(new PlayerResponse(player3Id)));
		assertEquals(3, playerResponses.size());
		
		//league2 should have 3 players, 1, 2, 4
		playerResponses = playersInLeagueRepository.findOne(league2.getId()).getPlayers();
		assertTrue(playerResponses.contains(new PlayerResponse(player1Id)));
		assertTrue(playerResponses.contains(new PlayerResponse(player2Id)));
		assertTrue(playerResponses.contains(new PlayerResponse(player4Id)));
		assertEquals(3, playerResponses.size());
		
		//league3 should have 1 player, 1
		playerResponses = playersInLeagueRepository.findOne(league3.getId()).getPlayers();
		assertTrue(playerResponses.contains(new PlayerResponse(player1Id)));
		assertEquals(1, playerResponses.size());
		
		//player 1 should be part of leagues 1,2,3
		Set<LeagueName> leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player1Id).getLeauges();
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertTrue(leagueNames.contains(new LeagueName(league3)));
		assertEquals(3, leagueNames.size());
		
		//player 2 should be part of leagues 1,2
		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player2Id).getLeauges();
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertEquals(2, leagueNames.size());
		
		//player 3 should be part of leagues 1
		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player3Id).getLeauges();
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertEquals(1, leagueNames.size());
		
		//player 4 should be part of leagues 2
		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player4Id).getLeauges();
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertEquals(1, leagueNames.size());
		
		 //player 5 should not be part of any leagues
		assertTrue(leaguesAPlayerHasJoinedRespository.findOne(player5Id) == null);
		
		//readd player 1 to league 1 and make sure he is not in there twice
		leagueService.addPlayerToLeague(league1, player1Id);
		
		//should be same as above
		playerResponses = playersInLeagueRepository.findOne(league1.getId()).getPlayers();
		assertTrue(playerResponses.contains(new PlayerResponse(player1Id)));
		assertTrue(playerResponses.contains(new PlayerResponse(player2Id)));
		assertTrue(playerResponses.contains(new PlayerResponse(player3Id)));
		assertEquals(3, playerResponses.size());
		
		//should be same as above
		leagueNames = leaguesAPlayerHasJoinedRespository.findOne(player1Id).getLeauges();
		assertTrue(leagueNames.contains(new LeagueName(league1)));
		assertTrue(leagueNames.contains(new LeagueName(league2)));
		assertTrue(leagueNames.contains(new LeagueName(league3)));
		assertEquals(3, leagueNames.size());
		
	}


}

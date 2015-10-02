package com.makeurpicks.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.makeurpicks.PicksApplication;
import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.GameResponse;
import com.makeurpicks.domain.LeagueResponse;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.domain.PickBuilder;
import com.makeurpicks.exception.PickValidationException;
import com.makeurpicks.exception.PickValidationException.PickExceptions;
import com.makeurpicks.repository.DoublePickRepository;
import com.makeurpicks.repository.PickRepository;
import com.makeurpicks.repository.PicksByLeagueWeekAndPlayerRepository;
import com.makeurpicks.repository.PicksByLeagueWeekRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PicksApplication.class)
@IntegrationTest({ "server.port:0", "spring.cloud.config.enabled:true" })
@WebAppConfiguration
public class PickServiceTest {

private static ConfigurableApplicationContext server;
	
	private static int configPort = 8888;
	
	@Mock
	private LeagueClient leagueClientMock;
	
	@Mock
	private GameClient gameClientMock;
	
	@InjectMocks
	@Autowired
	private PickService pickService;
	
	@Autowired 
	private PickRepository pickRepository;
	
	@Autowired
	private DoublePickRepository doublePickRepository;
	
	@Autowired
	private PicksByLeagueWeekRepository picksByLeagueWeekRepository;
	
	@Autowired
	private PicksByLeagueWeekAndPlayerRepository picksByLeagueWeekPlayerRepository;
	
	@Autowired
	private RedisTemplate<String, String> stringRedisTemplate;
	
	@BeforeClass
	public static void init() throws Exception {
//		String baseDir = ConfigServerTestUtils.getBaseDirectory("leagues");
		
//		String repo = ConfigServerTestUtils.prepareLocalRepo(baseDir, "target/repos",
//				"config-repo", "target/config");
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
		
		pickRepository.deleteAll();
		
		doublePickRepository.deleteAll();
		
		stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
		
		pickService.setGameClient(gameClientMock);
		
		pickService.setLeagueClient(leagueClientMock);
		
//		pickByLeagueWeekPlayerRepository.deleteAll();
//		
//		picksByLeagueWeekRepository.deleteAll();
	}
	
	@AfterClass
	public static void close() {
		System.clearProperty("config.port");
		if (server != null) {
			server.close();
		}
	}
	
	private void leagueClientMock(String leagueId)
	{
		when(leagueClientMock.getLeagueById(leagueId)).thenReturn(new LeagueResponse(leagueId));
	}
	
	private void gameClientMock(String gameId, String favId, String dogId, String weekId, long gameStartTime)
	{
		when(gameClientMock.getGameById(gameId)).thenReturn(new GameResponse(gameId, gameStartTime, favId, dogId, weekId));
	}
	
	
	@Test
	public void testMakePick() {
		String league1 ="l1";
		String league2 = "l2";
		
		String week1League1 = "w1";
		String week2League1 = "w2";
		
		String week1League2 = "w4";
		
		String game1League1Week1 = "g1";
		String game2League1Week1 = "g2";
		
		String game1League1Week2 = "g4";
		String game2League1Week2 = "g5";
		
		String game1League2Week1 = "g6";
		String game2League2Week1 = "g7";
		
		
		String player1 = "p1";
		String player2 = "p2";
		String player3 = "p3";
		String player4 = "p4";
		
		String team1 = "t1";
		String team2 = "t2";
		String team3 = "t3";
		String team4 = "t4";
		
		leagueClientMock(league1);
		leagueClientMock(league2);
		
		gameClientMock(game1League1Week1, team1, team2, week1League1, System.currentTimeMillis()-10000);
		gameClientMock(game1League1Week2, team1, team4, week2League1, System.currentTimeMillis()+10000);
		gameClientMock(game2League1Week1, team3, team4, week1League1, System.currentTimeMillis()+10000);
		gameClientMock(game2League1Week2, team3, team2, week2League1, System.currentTimeMillis()+10000);
		gameClientMock(game1League2Week1, team1, team2, week1League2, System.currentTimeMillis()+10000);
		gameClientMock(game2League2Week1, team3, team4, week1League2, System.currentTimeMillis()+10000);
		
		List<LeagueResponse> leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		leagues.add(new LeagueResponse(league2));
		when(leagueClientMock.getLeaguesForPlayer(player1)).thenReturn(leagues);
		
		leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		when(leagueClientMock.getLeaguesForPlayer(player2)).thenReturn(leagues);
		
		leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		when(leagueClientMock.getLeaguesForPlayer(player3)).thenReturn(leagues);
		
		leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league2));
		when(leagueClientMock.getLeaguesForPlayer(player4)).thenReturn(leagues);
		
		try
		{
			Pick pick1 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick1);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.GAME_HAS_ALREADY_STARTED.toString()));
		}
		
		try
		{
			Pick pick2 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick2);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.TEAM_NOT_PLAYING_IN_GAME.toString()));
		}
		
		try
		{
			Pick pick2 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player4)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick2);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.PLAYER_NOT_IN_LEAGUE.toString()));
		}
		
		try
		{
			Pick pick2 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick2);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.WEEK_IS_NOT_VALID.toString()));
		}
	}

	@Test
	public void testUpdatePick() {
		String league1 ="l1";
		String league2 = "l2";
		
		String week1League1 = "w1";
		
		
		String game1League1Week1 = "g1";
		String game2League1Week1 = "g2";
		
		String player1 = "p1";
		String player2 = "p2";
		String player3 = "p3";
		String player4 = "p4";
		
		String team1 = "t1";
		String team2 = "t2";
		String team3 = "t3";
		String team4 = "t4";
		
		leagueClientMock(league1);
		leagueClientMock(league2);
		
		
		List<LeagueResponse> leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		leagues.add(new LeagueResponse(league2));
		when(leagueClientMock.getLeaguesForPlayer(player1)).thenReturn(leagues);
		
		leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		when(leagueClientMock.getLeaguesForPlayer(player2)).thenReturn(leagues);
		
		leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		when(leagueClientMock.getLeaguesForPlayer(player3)).thenReturn(leagues);
		
		leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league2));
		when(leagueClientMock.getLeaguesForPlayer(player4)).thenReturn(leagues);
		
		gameClientMock(game1League1Week1, team1, team2, week1League1, System.currentTimeMillis()+5000);
		gameClientMock(game2League1Week1, team3, team4, week1League1, System.currentTimeMillis()+500000);
		
		Pick pick1 = new PickBuilder()
		.withGameId(game1League1Week1)
		.withLeagueId(league1)
		.withPlayerId(player1)
		.withTeamId(team1)
		.withWeekId(week1League1)
		.build();
		pickService.makePick(pick1);
		
		try{Thread.sleep(5000);}catch(Exception e){}
		pick1.setTeamId(team2);
		
		try
		{
			pickService.updatePick(pick1, player1);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.GAME_HAS_ALREADY_STARTED.toString()));
		}
		
		Pick pick2 = new PickBuilder()
		.withGameId(game2League1Week1)
		.withLeagueId(league1)
		.withPlayerId(player1)
		.withTeamId(team3)
		.withWeekId(week1League1)
		.build();
		pickService.makePick(pick2);
		
		try
		{
			pick2.setTeamId(team1);
			pickService.updatePick(pick2, player1);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.TEAM_NOT_PLAYING_IN_GAME.toString()));
		}
		
		pick2.setTeamId(team4);
		pickService.updatePick(pick2, player1);
			
		assertEquals(team4, pickRepository.findOne(pick2.getId()).getTeamId());

	}
	
		@Test
		public void testGetPicksByLeagueAndWeek() {
			String league1 ="l1";
			String league2 = "l2";
			
			String week1League1 = "w1";
			String week2League1 = "w2";
			
			String week1League2 = "w4";
			
			String game1League1Week1 = "g1";
			String game2League1Week1 = "g2";
			
			String game1League1Week2 = "g4";
			String game2League1Week2 = "g5";
			
			String game1League2Week1 = "g6";
			String game2League2Week1 = "g7";
			
			
			String player1 = "p1";
			String player2 = "p2";
			String player3 = "p3";
			String player4 = "p4";
			
			String team1 = "t1";
			String team2 = "t2";
			String team3 = "t3";
			String team4 = "t4";
			
			leagueClientMock(league1);
			leagueClientMock(league2);
			
			gameClientMock(game1League1Week1, team1, team2, week1League1, System.currentTimeMillis()+100000);
			gameClientMock(game1League1Week2, team1, team4, week2League1, System.currentTimeMillis()+100000);
			gameClientMock(game2League1Week1, team3, team4, week1League1, System.currentTimeMillis()+100000);
			gameClientMock(game2League1Week2, team3, team2, week2League1, System.currentTimeMillis()+100000);
			gameClientMock(game1League2Week1, team1, team2, week1League2, System.currentTimeMillis()+100000);
			gameClientMock(game2League2Week1, team3, team4, week1League2, System.currentTimeMillis()+100000);
			
			List<LeagueResponse> leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league1));
			leagues.add(new LeagueResponse(league2));
			when(leagueClientMock.getLeaguesForPlayer(player1)).thenReturn(leagues);
			
			leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league1));
			when(leagueClientMock.getLeaguesForPlayer(player2)).thenReturn(leagues);
			
			leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league1));
			when(leagueClientMock.getLeaguesForPlayer(player3)).thenReturn(leagues);
			
			leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league2));
			when(leagueClientMock.getLeaguesForPlayer(player4)).thenReturn(leagues);
			
			//league1, week1, team1 playing team2, team3 playing team4

			//fill out league 1, week 1 
			//player1 picks for week1, league1
			Pick pick1 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick1);
			
			Pick pick2 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team3)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick2);
			
			//player2 picks for week1, league 1
			Pick pick3 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team2)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick3);
			
			Pick pick4 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team4)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick4);
			
			//player3 pick for week1, league1
			Pick pick5 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick5);
			
			Pick pick6 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team4)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick6);
			
			//league1, week2, team1 playing team4, team3 playing team2
			//player1 picks for week2, league1
			Pick pick11 = new PickBuilder()
			.withGameId(game1League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick11);
			
			Pick pick12 = new PickBuilder()
			.withGameId(game2League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team2)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick12);
			
			//player2 picks for week2, league 1
			Pick pick13 = new PickBuilder()
			.withGameId(game1League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team4)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick13);
			
			Pick pick14 = new PickBuilder()
			.withGameId(game2League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team3)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick14);
			
			//player3 pick for week2, league1
			Pick pick15 = new PickBuilder()
			.withGameId(game1League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team4)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick15);
			
			Pick pick16 = new PickBuilder()
			.withGameId(game2League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team2)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick16);
			
			//start league2 
			//players 1 and 4
			//week1 should be same, team1 players team2, 
			Pick pick21 = new PickBuilder()
			.withGameId(game1League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player1)
			.withTeamId(team2)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick21);
			
			Pick pick22 = new PickBuilder()
			.withGameId(game2League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player1)
			.withTeamId(team4)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick22);
			
			Pick pick23 = new PickBuilder()
			.withGameId(game1League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player4)
			.withTeamId(team2)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick23);
			
			Pick pick24 = new PickBuilder()
			.withGameId(game2League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player4)
			.withTeamId(team3)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick24);
			
			List<Pick> picks = new ArrayList();
			for (Pick pick :pickService.getPicksByLeagueAndWeek(league1, week1League1))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick1));
			assertTrue(picks.contains(pick2));
			assertTrue(picks.contains(pick3));
			assertTrue(picks.contains(pick4));
			assertTrue(picks.contains(pick5));
			assertTrue(picks.contains(pick6));
			assertEquals(6, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueAndWeek(league1, week2League1))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick11));
			assertTrue(picks.contains(pick12));
			assertTrue(picks.contains(pick13));
			assertTrue(picks.contains(pick14));
			assertTrue(picks.contains(pick15));
			assertTrue(picks.contains(pick16));
			assertEquals(6, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueAndWeek(league2, week1League2))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick21));
			assertTrue(picks.contains(pick22));
			assertTrue(picks.contains(pick23));
			assertTrue(picks.contains(pick24));
			assertEquals(4, picks.size());
	
		}
	
		@Test
		public void testGetPicksByLeagueWeekAndPlayer() {
			String league1 ="l1";
			String league2 = "l2";
			
			String week1League1 = "w1";
			String week2League1 = "w2";
			
			String week1League2 = "w4";
			
			String game1League1Week1 = "g1";
			String game2League1Week1 = "g2";
			
			String game1League1Week2 = "g4";
			String game2League1Week2 = "g5";
			
			String game1League2Week1 = "g6";
			String game2League2Week1 = "g7";
			
			
			String player1 = "p1";
			String player2 = "p2";
			String player3 = "p3";
			String player4 = "p4";
			
			String team1 = "t1";
			String team2 = "t2";
			String team3 = "t3";
			String team4 = "t4";
			
			leagueClientMock(league1);
			leagueClientMock(league2);
			
			gameClientMock(game1League1Week1, team1, team2, week1League1, System.currentTimeMillis()+10000);
			gameClientMock(game1League1Week2, team1, team4, week2League1, System.currentTimeMillis()+10000);
			gameClientMock(game2League1Week1, team3, team4, week1League1, System.currentTimeMillis()+10000);
			gameClientMock(game2League1Week2, team3, team2, week2League1, System.currentTimeMillis()+10000);
			gameClientMock(game1League2Week1, team1, team2, week1League2, System.currentTimeMillis()+10000);
			gameClientMock(game2League2Week1, team3, team4, week1League2, System.currentTimeMillis()+10000);
			
			List<LeagueResponse> leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league1));
			leagues.add(new LeagueResponse(league2));
			when(leagueClientMock.getLeaguesForPlayer(player1)).thenReturn(leagues);
			
			leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league1));
			when(leagueClientMock.getLeaguesForPlayer(player2)).thenReturn(leagues);
			
			leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league1));
			when(leagueClientMock.getLeaguesForPlayer(player3)).thenReturn(leagues);
			
			leagues = new ArrayList<LeagueResponse>();
			leagues.add(new LeagueResponse(league2));
			when(leagueClientMock.getLeaguesForPlayer(player4)).thenReturn(leagues);
			
			//league1, week1, team1 playing team2, team3 playing team4

			//fill out league 1, week 1 
			//player1 picks for week1, league1
			Pick pick1 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick1);
			
			Pick pick2 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team3)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick2);
			
			//player2 picks for week1, league 1
			Pick pick3 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team2)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick3);
			
			Pick pick4 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team4)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick4);
			
			//player3 pick for week1, league1
			Pick pick5 = new PickBuilder()
			.withGameId(game1League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team1)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick5);
			
			Pick pick6 = new PickBuilder()
			.withGameId(game2League1Week1)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team4)
			.withWeekId(week1League1)
			.build();
			pickService.makePick(pick6);
			
			//league1, week2, team1 playing team4, team3 playing team2
			//player1 picks for week2, league1
			Pick pick11 = new PickBuilder()
			.withGameId(game1League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team1)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick11);
			
			Pick pick12 = new PickBuilder()
			.withGameId(game2League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player1)
			.withTeamId(team2)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick12);
			
			//player2 picks for week2, league 1
			Pick pick13 = new PickBuilder()
			.withGameId(game1League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team4)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick13);
			
			Pick pick14 = new PickBuilder()
			.withGameId(game2League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player2)
			.withTeamId(team3)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick14);
			
			//player3 pick for week2, league1
			Pick pick15 = new PickBuilder()
			.withGameId(game1League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team4)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick15);
			
			Pick pick16 = new PickBuilder()
			.withGameId(game2League1Week2)
			.withLeagueId(league1)
			.withPlayerId(player3)
			.withTeamId(team2)
			.withWeekId(week2League1)
			.build();
			pickService.makePick(pick16);
			
			//start league2 
			//players 1 and 4
			//week1 should be same, team1 players team2, 
			Pick pick21 = new PickBuilder()
			.withGameId(game1League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player1)
			.withTeamId(team2)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick21);
			
			Pick pick22 = new PickBuilder()
			.withGameId(game2League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player1)
			.withTeamId(team4)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick22);
			
			Pick pick23 = new PickBuilder()
			.withGameId(game1League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player4)
			.withTeamId(team2)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick23);
			
			Pick pick24 = new PickBuilder()
			.withGameId(game2League2Week1)
			.withLeagueId(league2)
			.withPlayerId(player4)
			.withTeamId(team3)
			.withWeekId(week1League2)
			.build();
			pickService.makePick(pick24);
			
			List<Pick> picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league1, week1League1, player1))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick1));
			assertTrue(picks.contains(pick2));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league1, week1League1, player2))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick3));
			assertTrue(picks.contains(pick4));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league1, week1League1, player3))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick5));
			assertTrue(picks.contains(pick6));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league1, week2League1, player1))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick11));
			assertTrue(picks.contains(pick12));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league1, week2League1, player2))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick13));
			assertTrue(picks.contains(pick14));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league1, week2League1, player3))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick15));
			assertTrue(picks.contains(pick16));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league2, week1League2, player1))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick21));
			assertTrue(picks.contains(pick22));
			assertEquals(2, picks.size());
			
			picks = new ArrayList<Pick>();
			for (Pick pick :pickService.getPicksByLeagueWeekAndPlayer(league2, week1League2, player4))
			{
				picks.add(pick);
			}
			assertTrue(picks.contains(pick23));
			assertTrue(picks.contains(pick24));
			assertEquals(2, picks.size());
	
		}
	
	@Test
	public void testMakeDoublePick() {

		String league1 ="l1";
	
		String week1League1 = "w1";
		
		
		String game1League1Week1 = "g1";
		String game2League1Week1 = "g2";
		String game3League1Week1 = "g3";
		
		String player1 = "p1";
	
		
		String team1 = "t1";
		String team2 = "t2";
		String team3 = "t3";
		String team4 = "t4";
		String team5 = "t5";
		String team6 = "t6";
		
		leagueClientMock(league1);
	
		List<LeagueResponse> leagues = new ArrayList<LeagueResponse>();
		leagues.add(new LeagueResponse(league1));
		when(leagueClientMock.getLeaguesForPlayer(player1)).thenReturn(leagues);
		
		
		gameClientMock(game1League1Week1, team1, team2, week1League1, System.currentTimeMillis()+5000);
		gameClientMock(game2League1Week1, team3, team4, week1League1, System.currentTimeMillis()+500000);
		gameClientMock(game3League1Week1, team5, team6, week1League1, System.currentTimeMillis()+500000);
		
		Pick pick1 = new PickBuilder()
		.withGameId(game1League1Week1)
		.withLeagueId(league1)
		.withPlayerId(player1)
		.withTeamId(team1)
		.withWeekId(week1League1)
		.build();
		pickService.makePick(pick1);
		
		try{Thread.sleep(5000);}catch(Exception e){}
		pick1.setTeamId(team2);
		
		//make double pick to a game that has already started
		try
		{
			pickService.makeDoublePick(pick1.getId(), player1);
			
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.GAME_HAS_ALREADY_STARTED.toString()));
		}
		
		Pick pick2 = new PickBuilder()
		.withGameId(game2League1Week1)
		.withLeagueId(league1)
		.withPlayerId(player1)
		.withTeamId(team3)
		.withWeekId(week1League1)
		.build();
		pickService.makePick(pick2);
		
		//set double pick, should be fine
		pickService.makeDoublePick(pick2.getId(), player1);
		DoublePick dp = doublePickRepository.findOne(DoublePick.buildString(league1, week1League1, player1));
		assertEquals(pick2.getId(), dp.getPickId());
		
		//add a new pick
		Pick pick3 = new PickBuilder()
		.withGameId(game3League1Week1)
		.withLeagueId(league1)
		.withPlayerId(player1)
		.withTeamId(team5)
		.withWeekId(week1League1)
		.build();
		pickService.makePick(pick3);
		
		//change to new game
		pickService.makeDoublePick(pick3.getId(), player1);
		dp = doublePickRepository.findOne(DoublePick.buildString(league1, week1League1, player1));
		assertEquals(pick3.getId(), dp.getPickId());

		//change the time of game2 to passed
		gameClientMock(game2League1Week1, team3, team4, week1League1, System.currentTimeMillis()-500000);
		try
		{
			pickService.makeDoublePick(pick2.getId(), player1);
			dp = doublePickRepository.findOne(DoublePick.buildString(league1, week1League1, player1));
			fail();
		}
		catch (PickValidationException exception)
		{
			assertTrue(exception.getMessage().contains(PickExceptions.GAME_HAS_ALREADY_STARTED.toString()));
		}
	
	}

}

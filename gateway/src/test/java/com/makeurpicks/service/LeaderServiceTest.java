package com.makeurpicks.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.makeurpicks.domain.ViewPickColumn;
import com.makeurpicks.service.game.GameIntegrationService;
import com.makeurpicks.service.game.GameView;
import com.makeurpicks.service.league.LeagueIntegrationService;
import com.makeurpicks.service.league.PlayerView;
import com.makeurpicks.service.pick.DoublePickView;
import com.makeurpicks.service.pick.PickIntegrationService;
import com.makeurpicks.service.pick.PickView;
import com.makeurpicks.team.TeamIntegrationService;
import com.makeurpicks.team.TeamView;

import rx.Observable;
import rx.observers.TestSubscriber;

public class LeaderServiceTest {

	@Autowired
	@InjectMocks
	private GatewayService gatewayService;
	
	@Mock
	private LeagueIntegrationService leagueClientMock;
	
	@Mock
	private GameIntegrationService gameClientMock;
	
	@Mock
	private PickIntegrationService pickClientMock;
	
	@Mock
	private TeamIntegrationService teamClientMock;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testgetPlayersPlusWinsInLeague()
	{
		String leagueId = "league1";
		String weekId = "week1";
		List<PlayerView> players = new ArrayList<>();
		players.add(new PlayerView("player1"));
		players.add(new PlayerView("player2"));
		players.add(new PlayerView("player3"));
		players.add(new PlayerView("player4"));
		
		when(leagueClientMock.getPlayersForLeague(leagueId)).thenReturn(Observable.just(players));
		
		
		List<GameView> games = new ArrayList<>();
		GameView game1 = new GameView();
		game1.setId("game1");
		game1.setDogId("tb");
		game1.setDogShortName("TB");
		game1.setFavId("bal");
		game1.setFavShortName("BAL");
		game1.setFavScore(22);
		game1.setDogScore(10);
		game1.setGameStart(ZonedDateTime.now().minusHours(1));
		games.add(game1);
		
		GameView game2 = new GameView();
		game2.setId("game2");
		game2.setDogId("stl");
		game2.setFavId("den");
		game2.setDogShortName("STL");
		game2.setFavShortName("DEN");
		game2.setFavScore(22);
		game2.setDogScore(10);
		game2.setGameStart(ZonedDateTime.now().minusHours(1));
		games.add(game2);
		
		GameView game3 = new GameView();
		game3.setId("game3");
		game3.setDogId("phl");
		game3.setFavId("nyg");
		game3.setDogShortName("PHL");
		game3.setFavShortName("NYG");
		game3.setFavScore(22);
		game3.setDogScore(10);
		game3.setGameStart(ZonedDateTime.now().minusHours(1));
		games.add(game3);
		
		GameView game4 = new GameView();
		game4.setId("game4");
		game4.setDogId("sf");
		game4.setFavId("sd");
		game4.setDogShortName("SF");
		game4.setFavShortName("SD");
		game4.setFavScore(22);
		game4.setDogScore(10);
		game4.setGameStart(ZonedDateTime.now().minusHours(1));
		games.add(game4);
		
		GameView game5 = new GameView();
		game5.setId("game5");
		game5.setDogId("hou");
		game5.setFavId("dal");
		game5.setDogShortName("HOU");
		game5.setFavShortName("DAL");
		game5.setFavScore(22);
		game5.setDogScore(10);
		game5.setGameStart(ZonedDateTime.now().minusHours(1));
		games.add(game5);
		
		GameView game6 = new GameView();
		game6.setId("game6");
		game6.setDogId("ne");
		game6.setFavId("nyj");
		game6.setDogShortName("NE");
		game6.setFavShortName("NYJ");
		game6.setFavScore(22);
		game6.setDogScore(10);
		game6.setGameStart(ZonedDateTime.now().minusHours(1));
		games.add(game6);
		
		when(gameClientMock.getGamesForWeek(weekId)).thenReturn(Observable.just(games));
		
		Map<String, DoublePickView> doubles = new HashMap<String, DoublePickView>();
		
		DoublePickView doublePick1 = new DoublePickView();
		doublePick1.setGameId(game1.getId());
		doubles.put("player1", doublePick1);
		
		DoublePickView doublePick2 = new DoublePickView();
		doublePick2.setGameId(game2.getId());
		doubles.put("player2", doublePick2);
		
		DoublePickView doublePick3 = new DoublePickView();
		doublePick3.setGameId(game3.getId());
		doubles.put("player3", doublePick3);
		
		when(pickClientMock.getAllDoublePickForPlayerForWeek(leagueId, weekId)).thenReturn(Observable.just(doubles));

		Map<String, Map<String, PickView>> picks = new HashMap<>();
		
		
		//player1 balx2, na, na, na, na, na = 3 win
		Map<String, PickView> picksByGame1 = new HashMap<>();
		
		PickView pick7 = new PickView();
		pick7.setTeamId("bal");
		picksByGame1.put(game1.getId(), pick7);
		
		PickView pick8 = new PickView();
		pick8.setTeamId("den");
		picksByGame1.put(game2.getId(), pick8);
		
		picks.put("player1", picksByGame1);
		
		//player2 tb, stl, phl, sd, hou, ne = 1 win
		Map<String, PickView> picksByGame2 = new HashMap<>();
		
		PickView pick9 = new PickView();
		pick9.setTeamId("tb");
		picksByGame2.put(game1.getId(), pick9);
		
		PickView pick10 = new PickView();
		pick10.setTeamId("stl");
		picksByGame2.put(game2.getId(), pick10);
		
		PickView pick11 = new PickView();
		pick11.setTeamId("phl");
		picksByGame2.put(game3.getId(), pick11);
		
		PickView pick12 = new PickView();
		pick12.setTeamId("sd");
		picksByGame2.put(game4.getId(), pick12);
		
		PickView pick13 = new PickView();
		pick13.setTeamId("hou");
		picksByGame2.put(game5.getId(), pick13);
		
		PickView pick14 = new PickView();
		pick14.setTeamId("ne");
		picksByGame2.put(game6.getId(), pick14);
		
		picks.put("player2", picksByGame2);
		
		//player3, bal, den, nygx2, sd, dal, nyj
		Map<String, PickView> picksByGame3 = new HashMap<>();
		
		PickView pick1 = new PickView();
		pick1.setTeamId("bal");
		picksByGame3.put(game1.getId(), pick1);
		
		PickView pick2 = new PickView();
		pick2.setTeamId("den");
		picksByGame3.put(game2.getId(), pick2);
		
		PickView pick3 = new PickView();
		pick3.setTeamId("nyg");
		picksByGame3.put(game3.getId(), pick3);
		
		PickView pick4 = new PickView();
		pick4.setTeamId("sd");
		picksByGame3.put(game4.getId(), pick4);
		
		PickView pick5 = new PickView();
		pick5.setTeamId("dal");
		picksByGame3.put(game5.getId(), pick5);
		
		PickView pick6 = new PickView();
		pick6.setTeamId("nyj");
		picksByGame3.put(game6.getId(), pick6);
		
		picks.put("player3", picksByGame3);
		
		//player4, bal, den, phl, sf, dal, njy win=4
		Map<String, PickView> picksByGame4 = new HashMap<>();
		
		PickView pick15 = new PickView();
		pick15.setTeamId("bal");
		picksByGame4.put(game1.getId(), pick15);
		
		PickView pick16 = new PickView();
		pick16.setTeamId("den");
		picksByGame4.put(game2.getId(), pick16);
		
		PickView pick17 = new PickView();
		pick17.setTeamId("phl");
		picksByGame4.put(game3.getId(), pick17);
		
		PickView pick18 = new PickView();
		pick18.setTeamId("sf");
		picksByGame4.put(game4.getId(), pick18);
		
		PickView pick19 = new PickView();
		pick19.setTeamId("dal");
		picksByGame4.put(game5.getId(), pick19);
		
		PickView pick20 = new PickView();
		pick20.setTeamId("nyj");
		picksByGame4.put(game6.getId(), pick20);
		
		picks.put("player4", picksByGame4);
		
		when(pickClientMock.getPicksForAllPlayerForWeek(leagueId, weekId)).thenReturn(Observable.just(picks));

		Map<String, TeamView> teamMap = new HashMap<>();
		
		TeamView ne = new TeamView();
		ne.setId("ne");
		ne.setShortName("NE");
		teamMap.put(ne.getId(), ne);
		
		TeamView tb = new TeamView();
		tb.setId("tb");
		tb.setShortName("TB");
		teamMap.put(tb.getId(), tb);
		
		TeamView bal = new TeamView();;
		bal.setId("bal");
		bal.setShortName("BAL");
		teamMap.put(bal.getId(), bal);
		
		TeamView stl = new TeamView();
		stl.setId("stl");
		stl.setShortName("STL");
		teamMap.put(stl.getId(), stl);
		
		TeamView den = new TeamView();
		den.setId("den");
		den.setShortName("DEN");
		teamMap.put(den.getId(), den);
		
		TeamView phl = new TeamView();
		phl.setId("phl");
		phl.setShortName("PHL");
		teamMap.put(phl.getId(), phl);
		
		TeamView nyg = new TeamView();
		nyg.setId("nyg");
		nyg.setShortName("NYG");
		teamMap.put(nyg.getId(), nyg);
		
		TeamView sf = new TeamView();
		sf.setId("sf");
		sf.setShortName("SF");
		teamMap.put(sf.getId(), sf);
		
		TeamView sd = new TeamView();
		sd.setId("sd");
		sd.setShortName("SD");
		teamMap.put(sd.getId(), sd);
		
		TeamView hou = new TeamView();
		hou.setId("hou");
		hou.setShortName("HOU");
		teamMap.put(hou.getId(), hou);
		
		TeamView dal = new TeamView();
		dal.setId("dal");
		dal.setShortName("DAL");
		teamMap.put(dal.getId(), dal);
		
		TeamView nyj = new TeamView();
		nyj.setId("nyj");
		nyj.setShortName("NYJ");
		teamMap.put(nyj.getId(), nyj);
		
		when(teamClientMock.getTeams()).thenReturn(Observable.just(teamMap));
		
		TestSubscriber<List<List<ViewPickColumn>>> testSubscriber = new TestSubscriber<>();
		gatewayService.getPlayersPlusWinsInLeague(leagueId, weekId).subscribe(testSubscriber);
		
		testSubscriber.assertNoErrors();
		List<List<ViewPickColumn>> rows = testSubscriber.getOnNextEvents().get(0);
		
		List<ViewPickColumn> column = rows.get(0);
		assertEquals("", column.get(0).getValue());
		assertEquals("player3(7)", column.get(1).getValue());
		assertEquals("player4(4)", column.get(2).getValue());
		assertEquals("player1(3)", column.get(3).getValue());
		assertEquals("player2(1)", column.get(4).getValue());
		
		
		//tb v bal 		
		column = rows.get(1);
		assertEquals("BAL vs TB", column.get(0).getValue());
		assertEquals("BAL", column.get(1).getValue());
		assertEquals("BAL", column.get(2).getValue());
		assertEquals("BAL", column.get(3).getValue());
		assertEquals("TB", column.get(4).getValue());
	
		
		//stl v den, 
		column = rows.get(2);
		assertEquals("DEN vs STL", column.get(0).getValue());
		assertEquals("DEN", column.get(1).getValue());
		assertEquals("DEN", column.get(2).getValue());
		assertEquals("DEN", column.get(3).getValue());
		assertEquals("STL", column.get(4).getValue());
		
		//phl v nyg, 
		column = rows.get(3);
		assertEquals("NYG vs PHL", column.get(0).getValue());
		assertEquals("NYG", column.get(1).getValue());
		assertEquals("PHL", column.get(2).getValue());
		assertEquals("X", column.get(3).getValue());
		assertEquals("PHL", column.get(4).getValue());
		
		//sf v sd, 
		column = rows.get(4);
		assertEquals("SD vs SF", column.get(0).getValue());
		assertEquals("SD", column.get(1).getValue());
		assertEquals("SF", column.get(2).getValue());
		assertEquals("X", column.get(3).getValue());
		assertEquals("SD", column.get(4).getValue());
		
		//hou v dal, 
		column = rows.get(5);
		assertEquals("DAL vs HOU", column.get(0).getValue());
		assertEquals("DAL", column.get(1).getValue());
		assertEquals("DAL", column.get(2).getValue());
		assertEquals("X", column.get(3).getValue());
		assertEquals("HOU", column.get(4).getValue());
	
		//ne v nyj
		column = rows.get(6);
		assertEquals("NYJ vs NE", column.get(0).getValue());
		assertEquals("NYJ", column.get(1).getValue());
		assertEquals("NYJ", column.get(2).getValue());
		assertEquals("X", column.get(3).getValue());
		assertEquals("NE", column.get(4).getValue());
		
		
		
		
	}
}

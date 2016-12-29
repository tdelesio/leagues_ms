package com.makeurpicks.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.makeurpicks.game.GameIntegrationService;
import com.makeurpicks.game.GameView;
import com.makeurpicks.game.WeekIntegrationService;
import com.makeurpicks.game.WeekView;
import com.makeurpicks.league.LeagueIntegrationService;
import com.makeurpicks.league.LeagueView;
import com.makeurpicks.league.PlayerLeagueView;
import com.makeurpicks.pick.DoublePickView;
import com.makeurpicks.pick.PickIntegrationService;
import com.makeurpicks.pick.PickView;
import com.makeurpicks.player.PlayerIntegrationService;
import com.makeurpicks.player.PlayerView;
import com.makeurpicks.season.SeasonIntegrationService;
import com.makeurpicks.season.SeasonView;
import com.makeurpicks.team.TeamIntegrationService;
import com.makeurpicks.team.TeamView;

import rx.Observer;

@Service
public class GameRandonizor {

	private Log log = LogFactory.getLog(GameRandonizor.class);
	
	@Autowired
	private GameIntegrationService gameIntegrationService;
	
	@Autowired
	private TeamIntegrationService teamIntegrationService;
	
	@Autowired
	private WeekIntegrationService weekIntegrationService;
	
	@Autowired
	private LeagueIntegrationService leagueIntegrationService;
	
	@Autowired
	private SeasonIntegrationService seasonIntegrationService;
	
	@Autowired
	private PlayerIntegrationService playerIntegrationService;
	
	@Autowired
	private PickIntegrationService pickIntegrationService;
	
//	public GameRandonizor()
	
	public GameView createRandomGame(WeekView week, TeamView fav, TeamView dog)
	{
		log.debug("createRandomGame");
		
		GameView game = new GameView();
		game.setWeekId(week.getId());
		game.setFavId(fav.getId());
		game.setFavFullName(fav.getFullTeamName());
		game.setFavShortName(fav.getShortName());
		game.setDogId(dog.getId());
		game.setDogFullName(dog.getFullTeamName());
		game.setDogShortName(dog.getShortName());
		game.setGameStart(ZonedDateTime.now().plusMinutes(5));
		game.setFavScore(new Random().nextInt(50)); 
		game.setDogScore(new Random().nextInt(50));
		
		int spread = new Random().nextInt(20);
		double halfpointspread = new Double(spread).doubleValue()+0.5;
		game.setSpread(halfpointspread);
		
		
		game.setFavHome(new Random().nextBoolean());
		
			
		
		game = gameIntegrationService.createGame(game);
		if (game == null || game.getId() == null)
			throw new RuntimeException();
		
		
		log.debug("createRandomGame game= "+game.toString());
		return game;
//		dummy.addGame(game);
	}
	
	public void createRandomWeek(List<PlayerView> players, WeekView weekView, String leagueId, int numberOfGames)
	{
		weekView = weekIntegrationService.createWeek(weekView);
		
		log.debug("createRandomWeek Week= "+weekView.toString());
		
//		TestSubscriber<List<Map<String, TeamView>>> testSubscriber = new TestSubscriber<>();
//		List<TeamView> teams = new ArrayList<>(teamIntegrationService.getTeams().toBlocking().single().values());
		List<TeamView> teams = new ArrayList<>(teamIntegrationService.getTeamsSync().values());
//		Map<String, TeamView> teamMap;
//		teamIntegrationService.getTeams().subscribe(new Observer<Map<String, TeamView>>() {
//			
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable throwable) {
//            }
//
//            @Override
//            public void onNext(Map<String, TeamView> t) {
//                teamMap = t;
//            }
//        });
//		teamIntegrationService.getTeams().toBlocking().
		
//		List<TeamView> teams = new ArrayList<>(teamMap.values());
		Collections.shuffle(teams);
		
		Map<String, Integer> doubleMap = new HashMap<>();
		for (PlayerView playerView:players)
		{
			int doubleIndex = new Random().nextInt(numberOfGames);
			if (numberOfGames==doubleIndex)
				continue;
			
			doubleMap.put(playerView.getUsername(), new Integer(doubleIndex));
		}
		
		for (int i=0; i<numberOfGames;i++)
		{
			
			TeamView fav = teams.remove(0);
			TeamView dog = teams.remove(0);
			
			
//			boolean gameStarted = new Random().nextBoolean();
//			boolean gameStarted = false;
//			boolean scoreEntered = false;
//			if (gameStarted)
//				scoreEntered = new Random().nextBoolean();
			
			log.debug("createRandomWeek weekView="+weekView+ "fav="+fav+" dog="+dog);
			GameView gameView = createRandomGame(weekView, fav, dog);
			 
			if (!gameView.getHasGameStarted())
			{	
				for (PlayerView playerView:players)
				{
					int noPick = new Random().nextInt(10);
					if (noPick == 5)
					{
						//player skips game
						continue;
					}
					
					boolean isFavPick = new Random().nextBoolean();
					String teamId = gameView.getFavId();
					if (isFavPick)
						teamId = gameView.getDogId();
					
					PickView pickView = new PickView();
					pickView.setGameId(gameView.getId());
					pickView.setLeagueId(leagueId);
					pickView.setPlayerId(playerView.getUsername());
					pickView.setWeekId(weekView.getId());
					pickView.setTeamId(teamId);
					
					pickView = pickIntegrationService.createPick(pickView);
					
					Integer doubleGame = doubleMap.get(playerView.getUsername());
					if (doubleGame != null && doubleGame.intValue() == i)
					{
						//game is double
						DoublePickView doublePickView = new DoublePickView();
						doublePickView.setGameId(gameView.getId());
						doublePickView.setLeagueId(leagueId);
						doublePickView.setPickId(pickView.getId());
						
						pickIntegrationService.createDoublePick(doublePickView);
					}
						
							
				}
			}
		}
	}
	
	public void createRandomLeague(int numberOfWeeks)
	{
		List<SeasonView> seasons = seasonIntegrationService.getCurrentSeasons();
		if (seasons==null || seasons.size()==0)
			throw new RuntimeException("No current seasons setup");
		
//		List<PlayerView> players = createUsers(new Random().nextInt(10)+5);
		
		String seasonId = seasons.get(0).getId();
		LeagueView leagueView = new LeagueView();
		leagueView.setLeagueName("demo "+new Random().nextInt(1000000));
		leagueView.setSeasonId(seasonId);
//		leagueView.setAdminId(players.get(new Random().nextInt(players.size()-1)).getUsername());
		leagueView.setAdminId("adminrandom");
		leagueView.setPassword("12345");
		
		leagueView = leagueIntegrationService.createLeague(leagueView);
		List<PlayerView> players = createUsers(new Random().nextInt(10)+5);
		//join league
		for (PlayerView playerView:players)
		{
			PlayerLeagueView playerLeagueView = new PlayerLeagueView();
			playerLeagueView.setLeagueId(leagueView.getId());
			playerLeagueView.setPlayerId(playerView.getUsername());
			playerLeagueView.setPassword("12345");
			leagueIntegrationService.addPlayerToLeague(playerLeagueView);
		}
		
		
		log.debug("League: "+leagueView.toString());
		
		if (numberOfWeeks == 0)
			numberOfWeeks = 17;
		
		for (int i=0;i<numberOfWeeks;i++)
		{
			WeekView weekView = new WeekView();
			weekView.setSeasonId(seasonId);
			weekView.setWeekNumber(i+1);
			
			int numberOfGame = 16;
			if (i>2&&i<9)
				numberOfGame = 14;
				
			createRandomWeek(players, weekView, leagueView.getId(), numberOfGame);
		}
		
		
	}
	
	private String randomString()
	{
		char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 8; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		return sb.toString();
	}
	
	public List<PlayerView> createUsers(int num_users)
	{
		
		List<PlayerView> players = new ArrayList<>();
		for (int i=0;i<num_users;i++)
		{
			String username = randomString();
			String email = username+"@gmail.com".replaceAll(" ", "");
			String password = "12345";
			PlayerView playerView = new PlayerView();
			playerView.setEmail(email);
			playerView.setUsername(username);
			playerView.setPassword(password);
		
			playerView = playerIntegrationService.createPlayer(playerView);
			players.add(playerView);
			
			log.fatal("Player: "+playerView.toString());
			
		}
		
		return players;
	}
}

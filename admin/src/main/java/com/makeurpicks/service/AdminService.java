package com.makeurpicks.service;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.game.GameIntegrationService;
import com.makeurpicks.game.GameView;
import com.makeurpicks.game.WeekIntegrationService;
import com.makeurpicks.game.WeekView;
import com.makeurpicks.league.LeagueIntegrationService;
import com.makeurpicks.league.LeagueView;
import com.makeurpicks.pick.DoublePickView;
import com.makeurpicks.pick.PickIntegrationService;
import com.makeurpicks.pick.PickView;
import com.makeurpicks.season.SeasonIntegrationService;
import com.makeurpicks.season.SeasonView;

@Component
public class AdminService {

	@Autowired
	private WeekIntegrationService weekIntegrationService;
	
	@Autowired 
	private SeasonIntegrationService seasonIntegrationService;
	
	@Autowired
	private LeagueIntegrationService leagueIntegrationService;
	
	@Autowired
	private GameIntegrationService gameIntegrationService;
	
	@Autowired
	private PickIntegrationService pickIntegrationService;
	
	public List<SeasonView> getSeasons() {
		return seasonIntegrationService.getCurrentSeasons();
	}
	
	public List<SeasonView> getCurrentSeasons() {
		return seasonIntegrationService.getCurrentSeasons();
	}
	
	public Dummy createDummyWeeks()
	{
		Dummy dummy = new Dummy();
		
//		SeasonView season = new SeasonView();
//		season.setLeagueType("pickem");
//		season.setStartYear(2015);
//		season.setEndYear(2016);
//		
//		season = seasonIntegrationService.createSeason(season);
//		if (season == null || season.getId() == null)
//			throw new RuntimeException();
//		
//		dummy.addSeason(season);
		
		SeasonView season = seasonIntegrationService.getCurrentSeasons().get(0);
		
		LeagueView leagueView = new LeagueView();
		leagueView.setLeagueName("Dummy League");
		leagueView.setSeasonId(season.getId());
		leagueView.setAdminId("admin");
		
		leagueView = leagueIntegrationService.createLeague(leagueView);
		if (leagueView == null || leagueView.getId()==null)
			throw new RuntimeException();
		dummy.addLeague(leagueView);
		
		WeekView week1 = new WeekView();
		week1.setWeekNumber(1);
		week1.setSeasonId(season.getId());
		
		week1 = weekIntegrationService.createWeek(week1);
		if (week1 == null || week1.getId() == null)
			throw new RuntimeException();
		dummy.addWeek(week1);
		
		WeekView week2 = new WeekView();
		week2.setWeekNumber(2);
		week2.setSeasonId(season.getId());
		
		week2 = weekIntegrationService.createWeek(week2);
		if (week2 == null || week2.getId() == null)
			throw new RuntimeException();
		dummy.addWeek(week2);
		
		WeekView week3 = new WeekView();
		week3.setWeekNumber(3);
		week3.setSeasonId(season.getId());
		
		week3 = weekIntegrationService.createWeek(week3);
		if (week3 == null || week3.getId() == null)
			throw new RuntimeException();
		dummy.addWeek(week3);
		
		WeekView week4 = new WeekView();
		week4.setWeekNumber(4);
		week4.setSeasonId(season.getId());
		
		week4 = weekIntegrationService.createWeek(week4);
		if (week4 == null || week4.getId() == null)
			throw new RuntimeException();
		dummy.addWeek(week4);
		
		WeekView week5 = new WeekView();
		week5.setWeekNumber(4);
		week5.setSeasonId(season.getId());
		
		week5 = weekIntegrationService.createWeek(week5);
		if (week5 == null || week5.getId() == null)
			throw new RuntimeException();
		dummy.addWeek(week5);
		
		
		
		//double pick must be gb
		GameView double_won = new GameView();
		double_won.setWeekId(week1.getId());
		double_won.setFavId("gb");
		double_won.setDogId("det");
		double_won.setGameStart(ZonedDateTime.now().plusDays(1));
		double_won.setFavScore(35); 
		double_won.setDogScore(22);
		double_won = gameIntegrationService.createGame(double_won);
		if (double_won == null || double_won.getId() == null)
			throw new RuntimeException();
		dummy.addGame(double_won);
		
		PickView double_won_pick = new PickView();
		double_won_pick.setGameId(double_won.getId());
		double_won_pick.setPlayerId("admin");
		double_won_pick.setWeekId(week1.getId());
		double_won_pick.setLeagueId(leagueView.getId());
		double_won_pick.setTeamId("gb");
		double_won_pick = pickIntegrationService.createPick(double_won_pick);
		if (double_won_pick == null || double_won_pick.getId() == null)
			throw new RuntimeException();
		dummy.addPick(double_won_pick);
		
		DoublePickView double_won_doublepick = new DoublePickView();
		double_won_doublepick.setGameId(double_won.getId());
		double_won_doublepick.setPickId(double_won_pick.getId());
		double_won_doublepick.setLeagueId(leagueView.getId());
		pickIntegrationService.createDoublePick(double_won_doublepick);
//		if (double_won_doublepick == null || double_won_doublepick.getId() == null)
//			throw new RuntimeException();		
//		dummy.addDouble(double_won_doublepick);
		
		double_won.setGameStart(ZonedDateTime.now().minusDays(2));
		gameIntegrationService.updateGame(double_won);
		
		//pick must be tb
		GameView won = new GameView();
		won.setWeekId(week1.getId());
		won.setFavId("tb");
		won.setDogId("atl");
		won.setGameStart(ZonedDateTime.now().minusDays(1));
		won.setFavScore(22);
		won.setDogScore(7);
		won = gameIntegrationService.createGame(won);
		if (won == null || won.getId() == null)
			throw new RuntimeException();
		dummy.addGame(won);
		
		PickView won_pick = new PickView();
		won_pick.setGameId(won.getId());
		won_pick.setPlayerId("admin");
		won_pick.setWeekId(week1.getId());
		won_pick.setTeamId("tb");
		won_pick.setLeagueId(leagueView.getId());
		won_pick = pickIntegrationService.createPick(won_pick);
		if (won_pick == null || won_pick.getId() == null)
			throw new RuntimeException();
		dummy.addPick(won_pick);
		
		//double pick on nyg
		GameView double_loss = new GameView();
		double_loss.setWeekId(week2.getId());
		double_loss.setFavId("nyj");
		double_loss.setDogId("nyg");
		double_loss.setGameStart(ZonedDateTime.now().plusDays(1));
		double_loss.setFavScore(9);
		double_loss.setDogScore(0);
		double_loss = gameIntegrationService.createGame(double_loss);
		if (double_loss == null || double_loss.getId() == null)
			throw new RuntimeException();
		dummy.addGame(double_loss);
		
		PickView double_loss_pick = new PickView();
		double_loss_pick.setGameId(double_loss.getId());
		double_loss_pick.setPlayerId("admin");
		double_loss_pick.setWeekId(week2.getId());
		double_loss_pick.setTeamId("nyg");
		double_loss_pick.setLeagueId(leagueView.getId());
		double_loss_pick = pickIntegrationService.createPick(double_loss_pick);
		if (double_loss_pick == null || double_loss_pick.getId() == null)
			throw new RuntimeException();
		dummy.addPick(double_loss_pick);
		
		DoublePickView double_loss_doublepick = new DoublePickView();
		double_loss_doublepick.setGameId(double_loss.getId());
		double_loss_doublepick.setPickId(double_loss_pick.getId());
		double_loss_doublepick.setLeagueId(leagueView.getId());
		pickIntegrationService.createDoublePick(double_loss_doublepick);
//		if (double_loss_doublepick == null || double_loss_doublepick.getId() == null)
//			throw new RuntimeException();
//		dummy.addDouble(double_loss_doublepick);
		
		double_loss.setGameStart(ZonedDateTime.now().minusDays(2));
		gameIntegrationService.updateGame(double_loss);
		
		
		//pick jac
		GameView loss = new GameView();
		loss.setWeekId(week1.getId());
		loss.setFavId("ten");
		loss.setDogId("jac");
		loss.setGameStart(ZonedDateTime.now().minusDays(1));
		loss.setFavScore(42);
		loss.setDogScore(39);
		loss = gameIntegrationService.createGame(loss);
		if (loss == null || loss.getId() == null)
			throw new RuntimeException(); 
		dummy.addGame(loss);
		
		PickView loss_pick = new PickView();
		loss_pick.setGameId(loss.getId());
		loss_pick.setPlayerId("admin");
		loss_pick.setTeamId("jac");
		loss_pick.setWeekId(week1.getId());
		loss_pick.setLeagueId(leagueView.getId());
		loss_pick = pickIntegrationService.createPick(loss_pick);
		if (loss_pick == null || loss_pick.getId() == null)
			throw new RuntimeException();
		dummy.addPick(loss_pick);
		
		//double pick sf
		GameView locked_double_pick = new GameView();
		locked_double_pick.setWeekId(week3.getId());
		locked_double_pick.setFavId("chi");
		locked_double_pick.setDogId("sf");
		locked_double_pick.setGameStart(ZonedDateTime.now().plusDays(1));
		locked_double_pick = gameIntegrationService.createGame(locked_double_pick);
		if (locked_double_pick == null || locked_double_pick.getId() == null)
			throw new RuntimeException();
		dummy.addGame(locked_double_pick);
		
		PickView locked_double_pick_pick = new PickView();
		locked_double_pick_pick.setGameId(locked_double_pick.getId());
		locked_double_pick_pick.setPlayerId("admin");
		locked_double_pick_pick.setTeamId("sf");
		locked_double_pick_pick.setLeagueId(leagueView.getId());
		locked_double_pick_pick.setWeekId(week3.getId());
		locked_double_pick_pick = pickIntegrationService.createPick(locked_double_pick_pick);
		if (locked_double_pick_pick == null || locked_double_pick_pick.getId() == null)
			throw new RuntimeException();
		dummy.addPick(locked_double_pick_pick);
		
		DoublePickView locked_double_pick_doublepick = new DoublePickView();
		locked_double_pick_doublepick.setGameId(locked_double_pick.getId());
		locked_double_pick_doublepick.setPickId(locked_double_pick_pick.getId());
		locked_double_pick_doublepick.setLeagueId(leagueView.getId());
		pickIntegrationService.createDoublePick(locked_double_pick_doublepick);
//		if (locked_double_pick_doublepick == null || locked_double_pick_doublepick.getId() == null)
//			throw new RuntimeException();
//		dummy.addDouble(locked_double_pick_doublepick);
		
		locked_double_pick.setGameStart(ZonedDateTime.now().minusDays(2));
		gameIntegrationService.updateGame(locked_double_pick);
		
		//pick buf
		GameView locked_pick = new GameView();
		locked_pick.setWeekId(week1.getId());
		locked_pick.setFavId("buf");
		locked_pick.setDogId("hou");
		locked_pick.setGameStart(ZonedDateTime.now().minusDays(1));
		locked_pick = gameIntegrationService.createGame(locked_pick);
		if (locked_pick == null || locked_pick.getId() == null)
			throw new RuntimeException();
		dummy.addGame(locked_pick);
		
		PickView locked_pick_pick = new PickView();
		locked_pick_pick.setGameId(locked_pick.getId());
		locked_pick_pick.setPlayerId("admin");
		locked_pick_pick.setTeamId("buf");
		locked_pick_pick.setWeekId(week1.getId());
		locked_pick_pick.setLeagueId(leagueView.getId());
		locked_pick_pick = pickIntegrationService.createPick(locked_pick_pick);
		if (locked_pick_pick == null || locked_pick_pick.getId()==null)
			throw new RuntimeException();
		dummy.addPick(locked_pick_pick);
		
		//no pick
		GameView no_pick_start = new GameView();
		no_pick_start.setWeekId(week1.getId());
		no_pick_start.setFavId("mia");
		no_pick_start.setDogId("bal");
		no_pick_start.setGameStart(ZonedDateTime.now().minusDays(1));
		no_pick_start = gameIntegrationService.createGame(no_pick_start);
		if (no_pick_start == null || no_pick_start.getId() == null)
			throw new RuntimeException();
		dummy.addGame(no_pick_start);
		
		//no pick
		GameView no_pick_over = new GameView();
		no_pick_over.setWeekId(week1.getId());
		no_pick_over.setFavId("stl");
		no_pick_over.setDogId("ari");
		no_pick_over.setFavScore(10);
		no_pick_over.setDogScore(20);
		no_pick_over.setGameStart(ZonedDateTime.now().minusDays(1));
		no_pick_over = gameIntegrationService.createGame(no_pick_over);
		if (no_pick_over == null || no_pick_over.getId() == null)
			throw new RuntimeException();
		dummy.addGame(no_pick_over);
		
		//double pick cin
		GameView double_pick = new GameView();
		double_pick.setWeekId(week4.getId());
		double_pick.setFavId("cle");
		double_pick.setDogId("cin");
		double_pick.setGameStart(ZonedDateTime.now().plusDays(90));
		double_pick = gameIntegrationService.createGame(double_pick);
		if (double_pick == null || double_pick.getId() == null)
			throw new RuntimeException();
		dummy.addGame(double_pick);
		
		PickView double_pick_pick = new PickView();
		double_pick_pick.setGameId(double_pick.getId());
		double_pick_pick.setPlayerId("admin");
		double_pick_pick.setTeamId("cin");
		double_pick_pick.setLeagueId(leagueView.getId());
		double_pick_pick.setWeekId(week4.getId());
		double_pick_pick = pickIntegrationService.createPick(double_pick_pick);
		if (double_pick_pick == null || double_pick_pick.getId() == null)
			throw new RuntimeException();
		dummy.addPick(double_pick_pick);
		
		DoublePickView double_pick_doublepick = new DoublePickView();
		double_pick_doublepick.setGameId(double_pick.getId());
		double_pick_doublepick.setPickId(double_pick_pick.getId());
		double_pick_doublepick.setLeagueId(leagueView.getId());
		pickIntegrationService.createDoublePick(double_pick_doublepick);
//		if (double_pick_doublepick == null || double_pick_doublepick.getId() == null)
//			throw new RuntimeException();
//		dummy.addDouble(double_pick_doublepick);
		
		
		//pick sea
		GameView pick = new GameView();
		pick.setWeekId(week1.getId());
		pick.setFavId("min");
		pick.setDogId("sea");
		pick.setGameStart(ZonedDateTime.now().plusDays(90));
		pick = gameIntegrationService.createGame(pick);
		if (pick == null || pick.getId() == null)
			throw new RuntimeException();
		dummy.addGame(pick);
		
		PickView pick_pick = new PickView();
		pick_pick.setGameId(pick.getId());
		pick_pick.setPlayerId("admin");
		pick_pick.setTeamId("sea");
		pick_pick.setWeekId(week1.getId());
		pick_pick.setLeagueId(leagueView.getId());
		pick_pick = pickIntegrationService.createPick(pick_pick);
		if (pick_pick==null ||pick_pick.getId()==null)
			throw new RuntimeException();
		dummy.addPick(pick_pick);
		
		
		//no pick
		GameView open = new GameView();
		open.setWeekId(week1.getId());
		open.setFavId("oak");
		open.setDogId("kc");
		open.setGameStart(ZonedDateTime.now().plusDays(90));
		open = gameIntegrationService.createGame(open);
		if (open == null || open.getId() == null)
			throw new RuntimeException();
		dummy.addGame(open);
		
		return dummy;
		
//			favstatus = "double"
//			favstatus = "pick";
//			favstatus = "not_pick";
//			dogstatus = "opponent";
//			favstatus = "doubleable";
//			dogstatus = "unpicked";
			
			
			
	}
}

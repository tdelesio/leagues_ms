package com.makeurpicks.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.domain.LeagueViewBuilder;
import com.makeurpicks.domain.PlayerView;
import com.makeurpicks.domain.PlayerViewBuilder;
import com.makeurpicks.domain.SeasonView;
import com.makeurpicks.domain.SeasonViewBuilder;
import com.makeurpicks.domain.View;

@Component
public class SetupLeagueService {

	@Autowired
	private SeasonClient seasonClient;
	
	@Autowired 
	private PlayerClient playerClient;
	
	@Autowired
	private LeagueClient leagueClient;
	
	public View setupLeague()
	{
		View view = new View();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
	
		seasonClient.createTeams();
		
		SeasonView season = new SeasonViewBuilder().withStartYear(calendar.get(Calendar.YEAR))
			.withEndYear(calendar.get(Calendar.YEAR)+1)
			.withLeagueType("PICKEM")
			.build();
	
		//setup season
		SeasonView seasonView = seasonClient.createSeason(season);
	
		view.setSeasonView(seasonView);
	
		//create player
		PlayerView player = new PlayerViewBuilder()
			.withEmail("tdelesio@gmail.com")
			.withPassword("rage311")
			.withUserName("tim")
			.asAdmin()
			.build();
		
		PlayerView playerView = playerClient.register(player);

		view.setPlayerView(playerView);

		//create league
		LeagueView league = new LeagueViewBuilder()
		.withAdminId(playerView.getId())
		.withName("pickem 2015")
		.withPassword("football")
		.withSeasonId(seasonView.getId())
		.build();
		
		LeagueView leagueView = leagueClient.createLeague(league);
	
		view.setLeagueView(leagueView);
		
		return view;
//	assertTrue(leagueService.getCurrentSeasons().contains(season));
//	assertEquals(league, leagueService.getLeagueById(league.getId()));
//	assertEquals(league, leagueService.getLeagueByName(league.getLeagueName()));
//	assertTrue(leagueService.getLeaguesForPlayer(playerId).contains(league));
	
	}

}

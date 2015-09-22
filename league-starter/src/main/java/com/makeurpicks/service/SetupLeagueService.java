package com.makeurpicks.service;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueBuilder;
import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.Player;
import com.makeurpicks.domain.PlayerBuilder;
import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.SeasonBuilder;

public class SetupLeagueService {

	@Autowired
	private SeasonClient seasonClient;
	
	@Autowired 
	private PlayerClient playerClient;
	
	@Autowired
	private LeagueClient leagueClient;
	

	public void setupLeague()
	{
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
	
		Season season = new SeasonBuilder().withStartYear(calendar.get(Calendar.YEAR))
			.withEndYear(calendar.get(Calendar.YEAR)+1)
			.withLeagueType(LeagueType.PICKEM)
			.build();
	
		//setup season
		Season seasonResponse = seasonClient.createSeason(season);
	
	
		//create player
		Player player = new PlayerBuilder()
			.withEmail("tdelesio@gmail.com")
			.withPassword("test123")
			.withUserName("tim")
			.asAdmin()
			.build();
		
		Player playerResponse = playerClient.register(player);

		//create league
		League league = new LeagueBuilder("1")
		.withAdminId(player.getId())
		.withName("test")
		.withPassword("pass")
		.withSeasonId(season.getId())
		.build();
		
		League leagueResponse = leagueClient.createLeague(league);
	
//	assertTrue(leagueService.getCurrentSeasons().contains(season));
//	assertEquals(league, leagueService.getLeagueById(league.getId()));
//	assertEquals(league, leagueService.getLeagueByName(league.getLeagueName()));
//	assertTrue(leagueService.getLeaguesForPlayer(playerId).contains(league));
	
	}

}

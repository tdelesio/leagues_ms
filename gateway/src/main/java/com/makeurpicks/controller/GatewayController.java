package com.makeurpicks.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.makeurpicks.domain.MakePicks;
import com.makeurpicks.domain.NavigationView;
import com.makeurpicks.service.game.GameIntegrationService;
import com.makeurpicks.service.league.LeagueIntegrationService;
import com.makeurpicks.service.pick.PickIntegrationService;
import com.makeurpicks.service.week.WeekIntegrationService;

import rx.Observable;

@RestController
public class GatewayController {

	@Autowired
	private GameIntegrationService gameIntegrationService;

	@Autowired
	private PickIntegrationService pickIntegrationService;

	@Autowired
	private LeagueIntegrationService leagueIntegrationService;

	@Autowired
	private WeekIntegrationService weekIntegrationService;

	MakePicks picks;
	
	@RequestMapping("/user")
    public Object home(Principal principal) {
		return principal;
    }
	
	@RequestMapping("/makepicks/{weekid}")
	 public DeferredResult<MakePicks> movieDetails(@PathVariable String weekId, Principal principal) {
		return buildMakePicks(principal.getName(), weekId);
	}

	@RequestMapping("/makepicks")
	public DeferredResult<MakePicks> makePicks(Principal principal) {

		return buildMakePicks(principal.getName(), null);
	}
	

	private DeferredResult<MakePicks> buildMakePicks(String userId, String weekId) {
	
		DeferredResult<MakePicks> result = new DeferredResult<>();
		MakePicks makePicksView = new MakePicks();
		
		emmitNavigation(userId, weekId)
			.subscribe(nav -> makePicksView.setNav(nav))
		;

		
		Observable.zip(gameIntegrationService.getGamesForWeek(makePicksView.getNav().getSelectedWeekId()),
				pickIntegrationService.getPicksForPlayerForWeek(makePicksView.getNav().getSelectedWeekId()), (games, picks) -> {
					
					makePicksView.setGames(games);
					makePicksView.setPicks(picks);
					return makePicksView;
				})
			.subscribe(n -> result.setResult(n));
		
		return result;
			
	}

	
	private Observable<NavigationView> emmitNavigation(String userId, String weekId)
	{
		NavigationView navigationView = new NavigationView();
		navigationView.setUsername(userId);
		
	
			leagueIntegrationService.getLeaguesForPlayer(userId)
				.filter(leagues -> leagues != null)
				.doOnNext(leagues -> navigationView.setSelectedSeasonId(leagues.get(0).getSeasonId()))
				.subscribe(leagues -> navigationView.setLeagues(leagues))			
			;
			
			
			weekIntegrationService.getWeeksForSeason(navigationView.getSelectedSeasonId())
				.filter(weeks -> weeks != null && !weeks.isEmpty())
				.doOnNext(weeks -> navigationView.setSelectedWeekId(weekId != null ? weekId:weeks.get(0).getId()))
				.subscribe(weeks -> navigationView.setWeeks(weeks))
			;
				
		return Observable.just(navigationView);	
	}
}

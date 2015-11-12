package com.makeurpicks.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.makeurpicks.domain.MakePicks;
import com.makeurpicks.domain.NavigationView;
import com.makeurpicks.service.game.GameIntegrationService;
import com.makeurpicks.service.league.LeagueIntegrationService;
import com.makeurpicks.service.league.LeagueView;
import com.makeurpicks.service.pick.PickIntegrationService;
import com.makeurpicks.service.week.WeekIntegrationService;
import com.makeurpicks.service.week.WeekView;

import rx.Observable;
import rx.Observer;

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
	// public DeferredResult<MakePicks> movieDetails(@PathVariable String
	// weekId, Principal principal)
	public DeferredResult<MakePicks> makePicksByWeek(@PathVariable String weekId, Principal principal) {
		return toDeferredResult(buildMakePicks(principal.getName(), weekId));
	}

	@RequestMapping("/makepicks2")
	// public DeferredResult<MakePicks> movieDetails(@PathVariable String
	// weekId, Principal principal)
	public MakePicks makePicks2(Principal principal) {

		MakePicks makePicksView = new MakePicks();
		makePicksView.setNav(buildNavigation(principal.getName()));
		return makePicksView;
//		buildMakePicks(principal.getName(), null).subscribe(new Observer<MakePicks>() {
//			@Override
//			public void onCompleted() {
//			}
//
//			@Override
//			public void onError(Throwable throwable) {
//			}
//
//			@Override
//			public void onNext(MakePicks makePicks) {
//				picks = makePicks;
//			}
//		});
//		
//		return picks;
	}
	
	
	@RequestMapping("/makepicks")
	// public DeferredResult<MakePicks> movieDetails(@PathVariable String
	// weekId, Principal principal)
	public DeferredResult<MakePicks> makePicks(Principal principal) {
		return toDeferredResult(buildMakePicks(principal.getName(), null));
	}

	public DeferredResult<MakePicks> toDeferredResult(Observable<MakePicks> details) {
		DeferredResult<MakePicks> result = new DeferredResult<>();
		details.subscribe(new Observer<MakePicks>() {
			@Override
			public void onCompleted() {
			}

			@Override
			public void onError(Throwable throwable) {
			}

			@Override
			public void onNext(MakePicks makePicks) {
				result.setResult(makePicks);
			}
		});
		return result;
	}

	private Observable<MakePicks> buildMakePicks(String userId, String weekId) {
		NavigationView navigationView = buildNavigation(userId);
		if (weekId == null)
			weekId = navigationView.getWeeks().get(0).getWeekId();

		return Observable.zip(gameIntegrationService.getGamesForWeek(weekId),
				pickIntegrationService.getPicksForPlayerForWeek(weekId), (games, picks) -> {
					MakePicks makePicksView = new MakePicks();
					makePicksView.setGames(games);
					makePicksView.setPicks(picks);
					makePicksView.setNav(navigationView);
					return makePicksView;
				});
	}

	private NavigationView buildNavigation(String userId) {
		NavigationView navigationView = new NavigationView();
		navigationView.setUsername(userId);
		List<LeagueView> leagues = leagueIntegrationService.getLeaguesForPlayer(userId);
		if (leagues!=null)
		{
			navigationView.setSelectedLeagueId(leagues.get(0).getLeagueId());
			navigationView.setLeagues(leagues);
			List<WeekView> weeks = weekIntegrationService.getWeeksForSeason(navigationView.getLeagues().get(0).getLeagueId());
			if (weeks!=null)
			{
				navigationView.setWeeks(weeks);
				navigationView.setSelectedWeekId(weeks.get(0).getWeekId());
			}
		}
		
		
		
		return navigationView;

	}
}

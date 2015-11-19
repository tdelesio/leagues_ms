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
import rx.functions.Action1;
import rx.observers.Observers;

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
	
//	@RequestMapping("/makepicks/{weekid}")
//	// public DeferredResult<MakePicks> movieDetails(@PathVariable String
//	// weekId, Principal principal)
//	public DeferredResult<MakePicks> makePicksByWeek(@PathVariable String weekId, Principal principal) {
//		return toDeferredResult(buildMakePicks(principal.getName(), weekId));
//	}

	@RequestMapping("/makepicks2")
	// public DeferredResult<MakePicks> movieDetails(@PathVariable String
	// weekId, Principal principal)
	public DeferredResult<NavigationView> makePicks2(Principal principal) {

		DeferredResult<NavigationView> result = new DeferredResult<>();
		emmitNavigation(principal.getName())
			.subscribe(n -> result.setResult(n));
		
		return result;
//		return buildNavigation(principal.getName());
//		MakePicks makePicksView = new MakePicks();
//		makePicksView.setNav(buildNavigation(principal.getName()));
//		return makePicksView;
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
	
	
//	@RequestMapping("/makepicks")
//	// public DeferredResult<MakePicks> movieDetails(@PathVariable String
//	// weekId, Principal principal)
//	public DeferredResult<MakePicks> makePicks(Principal principal) {
//		return toDeferredResult(buildMakePicks(principal.getName(), null));
//	}

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

//	private Observable<MakePicks> buildMakePicks(String userId, String weekId) {
//		NavigationView navigationView = buildNavigation(userId);
//		if (weekId == null)
//			weekId = navigationView.getWeeks().get(0).getWeekId();
//
//		return Observable.zip(gameIntegrationService.getGamesForWeek(weekId),
//				pickIntegrationService.getPicksForPlayerForWeek(weekId), (games, picks) -> {
//					MakePicks makePicksView = new MakePicks();
//					makePicksView.setGames(games);
//					makePicksView.setPicks(picks);
//					makePicksView.setNav(navigationView);
//					return makePicksView;
//				});
//	}

	
	private Observable<NavigationView> emmitNavigation(String userId)
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
				.doOnNext(weeks -> navigationView.setSelectedWeekId(weeks.get(0).getWeekId()))
				.subscribe(weeks -> navigationView.setWeeks(weeks))
			;
				
		return Observable.just(navigationView);	
	}
//	private Observable<NavigationView> buildNavigation(String userId) {
	private DeferredResult<NavigationView> buildNavigation(String userId) {
		
		Observable<List<LeagueView>> details = leagueIntegrationService.getLeaguesForPlayer(userId);
		
		DeferredResult<NavigationView> result = new DeferredResult<>();
		details.subscribe(new Observer<List<LeagueView>>() {
			@Override
			public void onCompleted() {
				System.out.println("completed");
			}

			@Override
			public void onError(Throwable throwable) {
			}

			@Override
			public void onNext(List<LeagueView> leagues) {
				System.out.println("onNext");
				
				NavigationView navigationView = new NavigationView();
				navigationView.setUsername(userId);
				if (leagues!=null)
				{
					navigationView.setSelectedSeasonId(leagues.get(0).getSeasonId());
					navigationView.setLeagues(leagues);
				}
				result.setResult(navigationView);
			}
		});
		
		
		
		
	
		
			
//			List<WeekView> weeks = weekIntegrationService.getWeeksForSeason(navigationView.getLeagues().get(0).getSeasonId());
//			if (weeks!=null)
//			{
//				navigationView.setWeeks(weeks);
//				navigationView.setSelectedWeekId(weeks.get(0).getWeekId());
//			}
	
		
		return result;

	}
}

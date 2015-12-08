package com.makeurpicks.controller;

import java.security.Principal;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.makeurpicks.domain.MakePicks;
import com.makeurpicks.domain.NavigationView;
import com.makeurpicks.service.game.GameIntegrationService;
import com.makeurpicks.service.league.LeagueIntegrationService;
import com.makeurpicks.service.pick.DoublePickView;
import com.makeurpicks.service.pick.PickIntegrationService;
import com.makeurpicks.service.week.WeekIntegrationService;

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
	
	@RequestMapping("/user")
    public Object home(Principal principal) {
		return principal;
    }
	
	@RequestMapping("/makepicks/{weekid}")
	 public DeferredResult<MakePicks> movieDetails(@PathVariable String weekid, Principal principal) {
//		return buildMakePicks(principal.getName(), weekId);
		return toDeferredResult(buildMakePicks(principal.getName(), weekid));
	}

	@RequestMapping("/makepicks")
	public DeferredResult<MakePicks> makePicks(Principal principal) {

//		return buildMakePicks(principal.getName(), null);
		return toDeferredResult(buildMakePicks(principal.getName(), null));
	}
	
	@RequestMapping("/makepicks2")
	public MakePicks makePicks2(Principal principal) {

//		return buildMakePicks(principal.getName(), null);
		return toNonDeferredResult(buildMakePicks(principal.getName(), null));
	}
	

	private Observable<MakePicks> buildMakePicks(String userId, String weekId) {
	
		DeferredResult<MakePicks> result = new DeferredResult<>();
		MakePicks makePicksView = new MakePicks();
		
		emmitNavigation(userId, weekId)
			.subscribe(nav -> makePicksView.setNav(nav))
		;

		
		return Observable.zip(
				gameIntegrationService.getGamesForWeek(makePicksView.getNav().getSelectedWeekId()),
				pickIntegrationService.getPicksForPlayerForWeek(makePicksView.getNav().getSelectedWeekId()),
				pickIntegrationService.getDoublePickForPlayerForWeek(makePicksView.getNav().getSelectedWeekId()),
				(games, picks, doublePick) -> {
					
					makePicksView.setGames(games);
					
					if (picks != null && picks.get("failure")!=null)
					{
						makePicksView.setHystrixFailureOnPicks(true);
						makePicksView.setPicks(Collections.EMPTY_MAP);
					}
					else
						makePicksView.setPicks(picks);
					makePicksView.setDoublePick(doublePick);
//					if (doublePick == null)
//						makePicksView.setDoublePick(new DoublePickView());
//					else		
						
					return makePicksView;
				});
//			.subscribe(n -> )
//			.subscribe(n -> result.setResult(n));
		
//		return result;
			
	}
	
	public MakePicks toNonDeferredResult(Observable<MakePicks> details)
	{
		MakePicks makePicks;
		return details.toBlocking().last();
//		details.subscribe(new Action1<MakePicks>() {
//		    @Override
//		    public void call(MakePicks picks) {
//		        makePicks = picks;
//		    }       
//		});   
		
//		return makePicks;
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

package com.makeurpicks.service.league;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class PlayersForLeagueObservableCommand extends HystrixObservableCommand<List<PlayerView>> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String leaugeId;
	
	public PlayersForLeagueObservableCommand(String id, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PlayersInLeague"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.leaugeId = id;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<List<PlayerView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<List<PlayerView>>() {
			
			@Override
			public void call(Subscriber<? super List<PlayerView>> observer) {
				observer.onNext(callLeagues());
//				observer.onCompleted();
			}
		});
	}
	
	@Override
	protected Observable<List<PlayerView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("cannot connect to nfl"));
		
		//fail silent
//		return Observable.empty();
	}
	
	
	private List<PlayerView> callLeagues()
	{
		ParameterizedTypeReference<List<PlayerView>> responseType = new ParameterizedTypeReference<List<PlayerView>>() {};
        List<PlayerView> players = secureRestTemplate.exchange("http://league/leagues/player/leagueid/{id}", HttpMethod.GET, null, responseType, leaugeId).getBody();
        return players;
	}
}

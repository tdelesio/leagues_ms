package com.makeurpicks.service.league;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

//@Service
public class LeagueObservableCommand extends HystrixObservableCommand<List<LeagueView>> {
	
//	@Autowired
//    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	private String id;
	
	public LeagueObservableCommand(String id, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("League"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.id = id;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<List<LeagueView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<List<LeagueView>>() {
			
			@Override
			public void call(Subscriber<? super List<LeagueView>> observer) {
				observer.onNext(callLeagues());
//				observer.onCompleted();
			}
		});
	}
	
	private List<LeagueView> callLeagues()
	{
		ParameterizedTypeReference<List<LeagueView>> responseType = new ParameterizedTypeReference<List<LeagueView>>() {};
        List<LeagueView> leagueViews = secureRestTemplate.exchange("http://league/leagues/player/{id}", HttpMethod.GET, null, responseType, id).getBody();
        return leagueViews;
	}

	@Override
	protected Observable<List<LeagueView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("Cannot connect to leagues"));
		
		//fail silent
//		return Observable.empty();
	}

}

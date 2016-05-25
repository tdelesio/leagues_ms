package com.makeurpicks.team;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class TeamsObservableCommand extends HystrixObservableCommand<Map<String, TeamView>> {

	private OAuth2RestOperations secureRestTemplate;
		
	public TeamsObservableCommand(OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Teams"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<Map<String, TeamView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<Map<String, TeamView>>() {
			
			@Override
			public void call(Subscriber<? super Map<String, TeamView>> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private Map<String, TeamView> callURL()
	{
		ParameterizedTypeReference<Map<String, TeamView>> responseType = new ParameterizedTypeReference<Map<String, TeamView>>() {};
      return secureRestTemplate.exchange("http://game/teams/", HttpMethod.GET, null, responseType).getBody();
	}

	@Override
	protected Observable<Map<String, TeamView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("Cannont get list of teams"));
		
		//fail silent
//		return Observable.empty();
	}
}

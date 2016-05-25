package com.makeurpicks.service.game;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class GameObservableCommand extends HystrixObservableCommand<List<GameView>> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String id;
	
	public GameObservableCommand(String id, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Games"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.id = id;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<List<GameView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<List<GameView>>() {
			
			@Override
			public void call(Subscriber<? super List<GameView>> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private List<GameView> callURL()
	{
		ParameterizedTypeReference<List<GameView>> responseType = new ParameterizedTypeReference<List<GameView>>() {};
        return secureRestTemplate.exchange("http://game/games/weekid/{id}", HttpMethod.GET, null, responseType, id).getBody();
	}

	@Override
	protected Observable<List<GameView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("Cannot call games"));
		
		//fail silent
//		return Observable.empty();
	}
}

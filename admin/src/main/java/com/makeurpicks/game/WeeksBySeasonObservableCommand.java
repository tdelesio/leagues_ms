package com.makeurpicks.game;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class WeeksBySeasonObservableCommand extends HystrixObservableCommand<List<WeekView>> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String id;
	
	public WeeksBySeasonObservableCommand(String id, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("WeeksBySeason"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.id = id;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<List<WeekView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<List<WeekView>>() {
			
			@Override
			public void call(Subscriber<? super List<WeekView>> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private List<WeekView> callURL()
	{
		ParameterizedTypeReference<List<WeekView>> responseType = new ParameterizedTypeReference<List<WeekView>>() {};
    	List<WeekView> weeks = secureRestTemplate.exchange("http://game/weeks/seasonid/{id}", HttpMethod.GET, null, responseType, id).getBody();
    	return weeks;
	}

	@Override
	protected Observable<List<WeekView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("Failed to get weeks by season"));
		
		//fail silent
//		return Observable.empty();
	}
}

package com.makeurpicks.service.week;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class WeekObservableCommand extends HystrixObservableCommand<List<WeekView>> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String id;
	
	public WeekObservableCommand(String id, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Week"))
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
    	return secureRestTemplate.exchange("http://game/weeks/seasonid/{id}", HttpMethod.GET, null, responseType, id).getBody();
	}

	@Override
	protected Observable<List<WeekView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("cannot connect to nfl"));
		
		//fail silent
//		return Observable.empty();
	}
}

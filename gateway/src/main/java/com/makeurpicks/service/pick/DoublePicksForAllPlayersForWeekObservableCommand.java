package com.makeurpicks.service.pick;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class DoublePicksForAllPlayersForWeekObservableCommand extends HystrixObservableCommand<Map<String, DoublePickView>> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String leagueid;
	private String weekid;
	
	public DoublePicksForAllPlayersForWeekObservableCommand(String lid, String wid, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DoublePicksForAllPlayersForWeek"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.leagueid = lid;
		this.weekid = wid;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<Map<String, DoublePickView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<Map<String, DoublePickView>>() {
			
			@Override
			public void call(Subscriber<? super Map<String, DoublePickView>> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private Map<String, DoublePickView> callURL()
	{
		ParameterizedTypeReference<Map<String, DoublePickView>> responseType = new ParameterizedTypeReference<Map<String, DoublePickView>>() {};
    	
    	return secureRestTemplate.exchange("http://pick/picks/doubles/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
	}

	@Override
	protected Observable<Map<String, DoublePickView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("cannot connect to nfl"));
		
		//fail silent
//		return Observable.empty();
	}
}

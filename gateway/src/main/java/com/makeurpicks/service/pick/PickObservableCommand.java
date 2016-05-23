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

public class PickObservableCommand extends HystrixObservableCommand<Map<String, PickView>> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String leagueid;
	private String weekid;
	
	public PickObservableCommand(String lid, String wid, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("Picks"))
				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)));
		
		this.leagueid = lid;
		this.weekid = wid;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<Map<String, PickView>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<Map<String, PickView>>() {
			
			@Override
			public void call(Subscriber<? super Map<String, PickView>> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private Map<String, PickView> callURL()
	{
		ParameterizedTypeReference<Map<String, PickView>> responseType = new ParameterizedTypeReference<Map<String, PickView>>() {};
        return secureRestTemplate.exchange("http://pick/picks/self/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();	
	}

	@Override
	protected Observable<Map<String, PickView>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("Cannot call picks"));
		
		//fail silent
//		return Observable.empty();
	}
}

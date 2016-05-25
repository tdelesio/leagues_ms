package com.makeurpicks.service.pick;

import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class DoublePickObservableCommand extends HystrixObservableCommand<DoublePickView> {

	private OAuth2RestOperations secureRestTemplate;
	
	private String leagueid;
	private String weekid;
	
	public DoublePickObservableCommand(String lid, String wid, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DoublePick"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.leagueid = lid;
		this.weekid = wid;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<DoublePickView> construct() {
	
		return Observable.create(new Observable.OnSubscribe<DoublePickView>() {
			
			@Override
			public void call(Subscriber<? super DoublePickView> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private DoublePickView callURL()
	{
		return secureRestTemplate.exchange("http://pick/picks/double/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, DoublePickView.class, leagueid, weekid).getBody();
	}

	@Override
	protected Observable<DoublePickView> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("Cannot call double pick"));
		
		//fail silent
//		return Observable.empty();
	}
}

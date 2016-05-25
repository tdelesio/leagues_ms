package com.makeurpicks.service.pick;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;
import rx.Subscriber;

public class PickForAllPlayersForWeekObservableCommand extends HystrixObservableCommand<Map<String, Map<String, PickView>>> {

	private Log log = LogFactory.getLog(PickForAllPlayersForWeekObservableCommand.class);
	
	private OAuth2RestOperations secureRestTemplate;
	
	private String leagueid;
	private String weekid;
	
	public PickForAllPlayersForWeekObservableCommand(String lid, String wid, OAuth2RestOperations secureRestTemplate)
	{
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("PickForAllPlayersForWeek"))
//				.andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000))
				);
		
		this.leagueid = lid;
		this.weekid = wid;
		this.secureRestTemplate = secureRestTemplate;
	}
	
	@Override
	protected Observable<Map<String, Map<String, PickView>>> construct() {
	
		return Observable.create(new Observable.OnSubscribe<Map<String, Map<String, PickView>>>() {
			
			@Override
			public void call(Subscriber<? super Map<String, Map<String, PickView>>> observer) {
				observer.onNext(callURL());
//				observer.onCompleted();
			}
		});
	}
	
	private Map<String, Map<String, PickView>> callURL()
	{
		ParameterizedTypeReference<Map<String, Map<String, PickView>>> responseType = new ParameterizedTypeReference<Map<String, Map<String, PickView>>>() {};
		Map<String, Map<String, PickView>> map = secureRestTemplate.exchange("http://pick/picks/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
		
		log.debug("callURL return="+map+ " leagueid="+leagueid+" weekdid="+weekid);
		return map;
	}

	@Override
	protected Observable<Map<String, Map<String, PickView>>> resumeWithFallback() {
		System.err.println("FAILURE");
		
		//fail fast
		return Observable.error(new Throwable("cannot connect to nfl"));
		
		//fail silent
//		return Observable.empty();
	}
}

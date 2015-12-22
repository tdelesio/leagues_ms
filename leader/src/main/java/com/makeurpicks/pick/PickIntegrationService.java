package com.makeurpicks.pick;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

import rx.Observable;

public class PickIntegrationService {

	private Log log = LogFactory.getLog(PickIntegrationService.class);
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	@HystrixCommand(fallbackMethod = "stubPicks",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<Map<String, Map<String, PickView>>> getPicksForPlayerForWeek(String leagueid, String weekid) {
        return new ObservableResult<Map<String, Map<String, PickView>>>() {
            @Override
            public Map<String, Map<String, PickView>> invoke() {
            	
//            	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
            	log.debug("leagueid="+leagueid+" weekid="+weekid);
            	ParameterizedTypeReference<Map<String, Map<String, PickView>>> responseType = new ParameterizedTypeReference<Map<String, Map<String, PickView>>>() {};
                return secureRestTemplate.exchange("http://pick/picks/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
//            	return Collections.emptyMap();                
            }
        };
    }

    @SuppressWarnings("unused")
    private Map<String, Map<String, PickView>> stubPicks(final String leagueId, final String weekId) {


    	throw new RuntimeException();
    }
    
    @HystrixCommand(fallbackMethod = "stubDoublePick",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<Map<String, DoublePickView>> getDoublePickForPlayerForWeek(String leagueid, String weekid) {
        return new ObservableResult<Map<String, DoublePickView>>() {
            @Override
            public Map<String, DoublePickView> invoke() {
            	
            	log.debug("leagueid="+leagueid+" weekid="+weekid);
            	ParameterizedTypeReference<Map<String, DoublePickView>> responseType = new ParameterizedTypeReference<Map<String, DoublePickView>>() {};
            	
            	return secureRestTemplate.exchange("http://pick/picks/doubles/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
//            	return Collections.emptyMap();                
            }
        };
    }

    @SuppressWarnings("unused")
    private DoublePickView stubDoublePick(final String leagueId, final String weekId) {
    	
    	throw new RuntimeException();
    }
}

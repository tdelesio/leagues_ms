package com.makeurpicks.service.pick;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

import rx.Observable;

@Service
public class PickIntegrationService {

	
private Log log = LogFactory.getLog(PickIntegrationService.class);

	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	@HystrixCommand(fallbackMethod = "stubPicks",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<Map<String, PickView>> getPicksForPlayerForWeek(String weekid) {
        return new ObservableResult<Map<String, PickView>>() {
            @Override
            public Map<String, PickView> invoke() {
            	
            	ParameterizedTypeReference<Map<String, PickView>> responseType = new ParameterizedTypeReference<Map<String, PickView>>() {};
                return secureRestTemplate.exchange("http://pick/picks/self/weekid/{weekid}", HttpMethod.GET, null, responseType, weekid).getBody();
//            	return Collections.emptyMap();                
            }
        };
    }

    @SuppressWarnings("unused")
    private Map<String, PickView> stubPicks(final String weekId) {


    	Map<String, PickView> map = new HashMap<>(1);
    	map.put("failure", new PickView(true));
    	
    	return map;
    }
    
    @HystrixCommand(fallbackMethod = "stubDoublePick",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<DoublePickView> getDoublePickForPlayerForWeek(String weekid) {
        return new ObservableResult<DoublePickView>() {
            @Override
            public DoublePickView invoke() {
            	
            	
            	return secureRestTemplate.exchange("http://pick/picks/double/weekid/{weekid}", HttpMethod.GET, null, DoublePickView.class, weekid).getBody();
//            	return Collections.emptyMap();                
            }
        };
    }

    @SuppressWarnings("unused")
    private DoublePickView stubDoublePick(final String weekId) {
    	
        return new DoublePickView(true);
    }
}


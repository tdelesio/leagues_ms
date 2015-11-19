package com.makeurpicks.service.week;

import java.util.Arrays;
import java.util.List;

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
public class WeekIntegrationService {

private Log log = LogFactory.getLog(WeekIntegrationService.class);

	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
//	@HystrixCommand(fallbackMethod = "stubWeeks",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
    public Observable<List<WeekView>> getWeeksForSeason(String id) {
//        return new ObservableResult<List<WeekView>>() {
//            @Override
//            public List<WeekView> invoke() {
//            	HttpHeaders headers = new HttpHeaders();
//            	headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//            	
//            	HttpEntity<?> entity = new HttpEntity<>(headers);
            	ParameterizedTypeReference<List<WeekView>> responseType = new ParameterizedTypeReference<List<WeekView>>() {};
            	List<WeekView> weeks = secureRestTemplate.exchange("http://game/weeks/seasonid/{id}", HttpMethod.GET, null, responseType, id).getBody();
            	return Observable.just(weeks);
                                
//            }
//        };
    }

    @SuppressWarnings("unused")
    private List<WeekView> stubWeeks(final String weekId) {
    	WeekView stub = new WeekView();
    	stub.setWeekNumber(0);
    	stub.setWeekId("0");
        return Arrays.asList(stub);
    }
}

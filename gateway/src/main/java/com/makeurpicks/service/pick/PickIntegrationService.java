package com.makeurpicks.service.pick;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.makeurpicks.service.league.LeagueView;
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
	
	@HystrixCommand(fallbackMethod = "stubPicks",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<Map<String, PickView>> getPicksForPlayerForWeek(String weekid) {
        return new ObservableResult<Map<String, PickView>>() {
            @Override
            public Map<String, PickView> invoke() {
//            	HttpHeaders headers = new HttpHeaders();
//            	headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//            	
//            	HttpEntity<?> entity = new HttpEntity<>(headers);
            	
            	ParameterizedTypeReference<Map<String, PickView>> responseType = new ParameterizedTypeReference<Map<String, PickView>>() {};
                return restTemplate.exchange("http://picks/self/weekid/{weekid}", HttpMethod.GET, null, responseType, weekid).getBody();
                                
            }
        };
    }

    @SuppressWarnings("unused")
    private List<LeagueView> stubPicks(final String weekId) {
    	LeagueView stub = new LeagueView();
    	stub.setLeagueId("0");
    	stub.setLeagueName("None");
        return Arrays.asList(stub);
    }
}


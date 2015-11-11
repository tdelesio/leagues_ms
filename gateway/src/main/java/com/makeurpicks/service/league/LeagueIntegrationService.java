package com.makeurpicks.service.league;

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

@Service
public class LeagueIntegrationService {

	private Log log = LogFactory.getLog(LeagueIntegrationService.class);

	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate unSecureRestTemplate;
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	@HystrixCommand(fallbackMethod = "stubLeagues",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public List<LeagueView> getLeaguesForPlayer(String id) {
//        return new ObservableResult<List<LeagueView>>() {
//            @Override
//            public List<LeagueView> invoke() {
//            	HttpHeaders headers = new HttpHeaders();
//            	headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//            	
//            	HttpEntity<?> entity = new HttpEntity<>(headers);
            	
            	ParameterizedTypeReference<List<LeagueView>> responseType = new ParameterizedTypeReference<List<LeagueView>>() {};
                List<LeagueView> leagueViews = secureRestTemplate.exchange("http://league/player/{id}", HttpMethod.GET, null, responseType, id).getBody();
                log.debug(leagueViews);
                return leagueViews;
                                
//            }
//        };
    }

    @SuppressWarnings("unused")
    private List<LeagueView> stubLeagues(String weekId) {
    	LeagueView stub = new LeagueView();
    	stub.setLeagueId("0");
    	stub.setLeagueName("None");
        return Arrays.asList(stub);
    }
	
}

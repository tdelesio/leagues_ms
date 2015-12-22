package com.makeurpicks.league;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

import rx.Observable;

@Service
public class LeagueIntegrationService {

	private Log log = LogFactory.getLog(LeagueIntegrationService.class);
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	
	
	
	@HystrixCommand(fallbackMethod = "defaultGetPlayersForLeague"
			,commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<List<PlayerView>> getPlayersForLeague(String id) {

		
		return new ObservableResult<List<PlayerView>>() {
            @Override
            public List<PlayerView> invoke() {
        	
            	log.debug("leagueId="+id);
            	ParameterizedTypeReference<List<PlayerView>> responseType = new ParameterizedTypeReference<List<PlayerView>>() {};
                List<PlayerView> players = secureRestTemplate.exchange("http://league/player/leagueid/{id}", HttpMethod.GET, null, responseType, id).getBody();
                return players;                             
            }
        };
    }

    @SuppressWarnings("unused")
    public List<PlayerView> defaultGetPlayersForLeague(String id) {
    	PlayerView stub = new PlayerView();
    	stub.setId("ERROR");
        return Arrays.asList(stub);
    }
}

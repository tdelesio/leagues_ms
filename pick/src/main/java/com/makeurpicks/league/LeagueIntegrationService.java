package com.makeurpicks.league;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class LeagueIntegrationService {

	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;

    @HystrixCommand(fallbackMethod = "stubLeague",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
	public @ResponseBody LeagueResponse getLeagueById(String id)
	{
    	final LeagueResponse response = secureRestTemplate.getForObject("http://league/leagues/{id}", LeagueResponse.class, id);
        return response;
	}
    
    @SuppressWarnings("unused")
    private LeagueResponse stubLeague(final String id) {
    	LeagueResponse response = new LeagueResponse();
    	response.setId(id);
	      return response;
    }
	
    @HystrixCommand(fallbackMethod = "stubLeagues",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
	public @ResponseBody List<LeagueResponse> getLeaguesForPlayer(String playerId)
	{
		ParameterizedTypeReference<List<LeagueResponse>> responseType = new ParameterizedTypeReference<List<LeagueResponse>>() {};
        return secureRestTemplate.exchange("http://league/leagues/player/playerId/{id}", HttpMethod.GET, null, responseType, playerId).getBody();
	}
    
    @SuppressWarnings("unused")
    private List<LeagueResponse> stubLeagues(final String weekId) {
    	List<LeagueResponse> response = new ArrayList<>();
    	LeagueResponse stub = new LeagueResponse();
    	response.add(stub);
        return response;
    }
}

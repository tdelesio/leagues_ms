package com.makeurpicks.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.exception.GameValidationException;
import com.makeurpicks.exception.GameValidationException.GameExceptions;
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
	public @ResponseBody LeagueView getLeagueById(String id)
	{
    	final LeagueView response = secureRestTemplate.getForObject("http://league/leagues/{id}", LeagueView.class, id);
        return response;
	}
    
    @SuppressWarnings("unused")
    private LeagueView stubLeague(final String id) {
    	throw new GameValidationException(GameExceptions.LEAGUE_SERVICE_DOWN);
    }
}

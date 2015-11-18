package com.makeurpicks.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class GameIntegrationService {

	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "stubGame",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public GameResponse getGameById(String id)
    {
    	final GameResponse response = restTemplate.getForObject("http://gateway/games/{id}", GameResponse.class, id);
        return response;
    }
    
    @SuppressWarnings("unused")
    private GameResponse stubGame(final String id) {
    	GameResponse response = new GameResponse();
    	response.setId(id);
	      return response;
    }
}

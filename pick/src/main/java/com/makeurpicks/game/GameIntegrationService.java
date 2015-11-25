package com.makeurpicks.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.makeurpicks.exception.PickValidationException;
import com.makeurpicks.exception.PickValidationException.PickExceptions;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class GameIntegrationService {

	private Log log = LogFactory.getLog(GameIntegrationService.class);
	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;

	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
    @HystrixCommand(fallbackMethod = "stubGame",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public GameResponse getGameById(String id)
    {
//    	final GameResponse response = secureRestTemplate.getForObject("http://game/games/{id}", GameResponse.class, id);
    	log.debug("gameId = "+id);

    	final GameResponse response = secureRestTemplate.exchange("http://game/games/{id}", HttpMethod.GET, null, GameResponse.class, id).getBody();
        return response;
    }
    
    @SuppressWarnings("unused")
    private GameResponse stubGame(final String id) {
    	throw new PickValidationException(PickExceptions.GAME_SERVICE_IS_DOWN);
    }
}

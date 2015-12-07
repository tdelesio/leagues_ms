package com.makeurpicks.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
	
    
    public GameView createGame(GameView gameView)
    {
    	return secureRestTemplate.postForEntity("http://game/games/", gameView, GameView.class).getBody();
    }
    
    public void updateGame(GameView gameView)
    {
    	secureRestTemplate.put("http://game/games/", gameView);
    }
}

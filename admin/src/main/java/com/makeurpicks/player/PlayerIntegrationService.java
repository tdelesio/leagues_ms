package com.makeurpicks.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Service
public class PlayerIntegrationService {

	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;

    
    
    public PlayerView createPlayer(PlayerView PlayerView)
    {
    	return secureRestTemplate.postForEntity("http://player/auth/players/", PlayerView, PlayerView.class).getBody();
    }
}

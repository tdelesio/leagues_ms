package com.makeurpicks.league;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Service
public class LeagueIntegrationService {

private Log log = LogFactory.getLog(LeagueIntegrationService.class);


	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;

    
    
    public LeagueView createLeague(LeagueView leagueView)
    {
    	return secureRestTemplate.postForEntity("http://league/leagues/", leagueView, LeagueView.class).getBody();
    }
    
	public void addPlayerToLeague(PlayerLeagueView playerLeague)
	{
    	secureRestTemplate.postForEntity("http://league/leagues/player/admin", playerLeague, PlayerLeagueView.class).getBody();	
	}
}

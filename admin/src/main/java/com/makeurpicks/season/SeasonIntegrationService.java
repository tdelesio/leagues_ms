package com.makeurpicks.season;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Service
public class SeasonIntegrationService {

private Log log = LogFactory.getLog(SeasonIntegrationService.class);


	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;

    
    
    public SeasonView createSeason(SeasonView seasonView)
    {
    	return secureRestTemplate.postForEntity("http://league/leagues/seasons/", seasonView, SeasonView.class).getBody();
    }
    
    public List<SeasonView> getCurrentSeasons()
    {
    	ParameterizedTypeReference<List<SeasonView>> responseType = new ParameterizedTypeReference<List<SeasonView>>() {};
    	List<SeasonView> seasons = secureRestTemplate.exchange("http://league/leagues/seasons/current", HttpMethod.GET, null, responseType).getBody();
    	return seasons;
    }
}

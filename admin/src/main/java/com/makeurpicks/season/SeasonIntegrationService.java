package com.makeurpicks.season;

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

import com.makeurpicks.game.WeekView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

import rx.Observable;

@Service
public class SeasonIntegrationService {

private Log log = LogFactory.getLog(SeasonIntegrationService.class);


	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;

    
    
    public SeasonView createSeason(SeasonView seasonView)
    {
    	return secureRestTemplate.postForEntity("http://season/seasons/", seasonView, SeasonView.class).getBody();
    }
    
    public List<SeasonView> getCurrentSeasons()
    {
    	ParameterizedTypeReference<List<SeasonView>> responseType = new ParameterizedTypeReference<List<SeasonView>>() {};
    	List<SeasonView> seasons = secureRestTemplate.exchange("http://season/seasons/current", HttpMethod.GET, null, responseType).getBody();
    	return seasons;
    }
}

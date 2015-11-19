package com.makeurpicks.service.game;

import java.util.ArrayList;
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

import com.makeurpicks.service.league.LeagueView;
import com.makeurpicks.service.week.WeekView;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

import rx.Observable;

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
	
	@HystrixCommand(fallbackMethod = "stubGames",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public Observable<List<GameView>> getGamesForWeek(final String id) {
        return new ObservableResult<List<GameView>>() {
            @Override
            public List<GameView> invoke() {
                
                ParameterizedTypeReference<List<GameView>> responseType = new ParameterizedTypeReference<List<GameView>>() {};
                return secureRestTemplate.exchange("http://game/games/weekid/{id}", HttpMethod.GET, null, responseType, id).getBody();         
            }
        };
    }

    @SuppressWarnings("unused")
    private List<GameView> stubGames(final String weekId) {
    	List<GameView> games = new ArrayList<>();
        GameView stub = new GameView();
        games.add(stub);
        return games;
    }
}

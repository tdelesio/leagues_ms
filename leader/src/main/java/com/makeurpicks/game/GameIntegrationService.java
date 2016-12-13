package com.makeurpicks.game;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;


@Service
public class GameIntegrationService {

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

package com.makeurpicks.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.makeurpicks.domain.PlayerResponse;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.ObservableResult;

import rx.Observable;

@Service
public class PlayerIntegrationService {

	private Log log = LogFactory.getLog(PlayerIntegrationService.class);
	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "stubPlayer",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
            }
    )
    public PlayerResponse getPlayer(final String id) {
//	        return new ObservableResult<PlayerResponse>() {
//	            @Override
//	            public PlayerResponse invoke() {
                final PlayerResponse movie = restTemplate.getForObject("http://gateway/user/{id}", PlayerResponse.class, id);
                log.debug(movie);
                return movie;
//            }
//        };
    }

    @SuppressWarnings("unused")
    private PlayerResponse stubPlayer(final String id) {
	      PlayerResponse playerResponse = new PlayerResponse();
	      playerResponse.setId(id);
	      return playerResponse;
    }
}

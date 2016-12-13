package com.makeurpicks.service.league;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import rx.Observable;

@Service
public class LeagueIntegrationService {

	private Log log = LogFactory.getLog(LeagueIntegrationService.class);

	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate unSecureRestTemplate;
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	
	
	
//	@HystrixCommand(fallbackMethod = "defaultGetPlayersForLeague"
//			,commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
    public Observable<List<PlayerView>> getPlayersForLeague(String id) {
    	return new PlayersForLeagueObservableCommand(id, secureRestTemplate).observe();
		
//		return new ObservableResult<List<PlayerView>>() {
//            @Override
//            public List<PlayerView> invoke() {
//        	
//            	log.debug("leagueId="+id);
//            	ParameterizedTypeReference<List<PlayerView>> responseType = new ParameterizedTypeReference<List<PlayerView>>() {};
//                List<PlayerView> players = secureRestTemplate.exchange("http://league/leagues/player/leagueid/{id}", HttpMethod.GET, null, responseType, id).getBody();
//                return players;                             
//            }
//        };
    }

//    @SuppressWarnings("unused")
//    public List<PlayerView> defaultGetPlayersForLeague(String id) {
//    	PlayerView stub = new PlayerView();
//    	stub.setId("ERROR");
//        return Arrays.asList(stub);
//    }
    
    
	
//	@HystrixCommand(fallbackMethod = "defaultGetLeaguesForPlayer"
//			,commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
    public Observable<List<LeagueView>> getLeaguesForPlayer(String id) {
    	return new LeagueObservableCommand(id, secureRestTemplate).observe();
    }
//    public Observable<List<LeagueView>> getLeaguesForPlayer(String id) {
//
//		
//		return new ObservableResult<List<LeagueView>>() {
//            @Override
//            public List<LeagueView> invoke() {
//        	
//            	ParameterizedTypeReference<List<LeagueView>> responseType = new ParameterizedTypeReference<List<LeagueView>>() {};
//                List<LeagueView> leagueViews = secureRestTemplate.exchange("http://league/leagues/player/{id}", HttpMethod.GET, null, responseType, id).getBody();
//                return leagueViews;                             
//            }
//        };
//    }
//
//    @SuppressWarnings("unused")
//    public List<LeagueView> defaultGetLeaguesForPlayer(String id) {
//    	LeagueView stub = new LeagueView();
//    	stub.setLeagueId("0");
//    	stub.setLeagueName("None");
//        return Arrays.asList(stub);
//    }
	
}

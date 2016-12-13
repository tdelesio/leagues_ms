package com.makeurpicks.team;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

import rx.Observable;

@Service
public class TeamIntegrationService {

	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	public Observable<Map<String, TeamView>> getTeams() {
		return new TeamsObservableCommand(secureRestTemplate).observe();
	}
	
	public Map<String, TeamView> getTeamsSync() {
		ParameterizedTypeReference<Map<String, TeamView>> responseType = new ParameterizedTypeReference<Map<String, TeamView>>() {};
	      return secureRestTemplate.exchange("http://game/teams/", HttpMethod.GET, null, responseType).getBody();
	}
	
//	@HystrixCommand(fallbackMethod = "stubTeams",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
//    public Observable<Map<String, TeamView>> getTeams() {
//        return new ObservableResult<Map<String, TeamView>>() {
//            @Override
//            public Map<String, TeamView> invoke() {
//                
//                ParameterizedTypeReference<Map<String, TeamView>> responseType = new ParameterizedTypeReference<Map<String, TeamView>>() {};
//                return secureRestTemplate.exchange("http://game/teams/", HttpMethod.GET, null, responseType).getBody();         
//            }
//        };
//    }
//
//    @SuppressWarnings("unused")
//    private Map<String, TeamView> stubTeams() {
//    	return Collections.EMPTY_MAP;
//    }
}

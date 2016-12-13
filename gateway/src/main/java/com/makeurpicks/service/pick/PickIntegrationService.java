package com.makeurpicks.service.pick;

import java.util.Map;

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
public class PickIntegrationService {

	
private Log log = LogFactory.getLog(PickIntegrationService.class);

	
	@Autowired
    @Qualifier("loadBalancedRestTemplate")
    @LoadBalanced
    RestTemplate restTemplate;
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	public Observable<Map<String, PickView>> getPicksForPlayerForWeek(String leagueid, String weekid) {
		
		return new PickObservableCommand(leagueid, weekid, secureRestTemplate).observe();
	}
	
	public Observable<DoublePickView> getDoublePickForPlayerForWeek(String leagueid, String weekid) {
		return new DoublePickObservableCommand(leagueid, weekid, secureRestTemplate).observe();
	}
	
	public Observable<Map<String, Map<String, PickView>>> getPicksForAllPlayerForWeek(String leagueid, String weekid) {
		return new PickForAllPlayersForWeekObservableCommand(leagueid, weekid, secureRestTemplate).observe();
	}
	
	public Observable<Map<String, DoublePickView>> getAllDoublePickForPlayerForWeek(String leagueid, String weekid) {
		return new DoublePicksForAllPlayersForWeekObservableCommand(leagueid, weekid, secureRestTemplate).observe();
	}
//	@HystrixCommand(fallbackMethod = "stubPicks",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
//    public Observable<Map<String, PickView>> getPicksForPlayerForWeek(String leagueid, String weekid) {
//        return new ObservableResult<Map<String, PickView>>() {
//            @Override
//            public Map<String, PickView> invoke() {
//            	
////            	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//            	log.debug("leagueid="+leagueid+" weekid="+weekid);
//            	ParameterizedTypeReference<Map<String, PickView>> responseType = new ParameterizedTypeReference<Map<String, PickView>>() {};
//                return secureRestTemplate.exchange("http://pick/picks/self/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
////            	return Collections.emptyMap();                
//            }
//        };
//    }
//
//    @SuppressWarnings("unused")
//    private Map<String, PickView> stubPicks(final String leagueId, final String weekId) {
//
//
//    	Map<String, PickView> map = new HashMap<>(1);
//    	map.put("failure", new PickView(true));
//    	
//    	return map;
//    }
    
//    @HystrixCommand(fallbackMethod = "stubDoublePick",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
//    public Observable<DoublePickView> getDoublePickForPlayerForWeek(String leagueid, String weekid) {
//        return new ObservableResult<DoublePickView>() {
//            @Override
//            public DoublePickView invoke() {
//            	
//            	log.debug("leagueid="+leagueid+" weekid="+weekid);
//            	return secureRestTemplate.exchange("http://pick/picks/double/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, DoublePickView.class, leagueid, weekid).getBody();
////            	return Collections.emptyMap();                
//            }
//        };
//    }
//
//    @SuppressWarnings("unused")
//    private DoublePickView stubDoublePick(final String leagueId, final String weekId) {
//    	
//        return new DoublePickView(true);
//    }
    
    
    
//    @HystrixCommand(fallbackMethod = "stubAllPicks",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
//    public Observable<Map<String, Map<String, PickView>>> getPicksForAllPlayerForWeek(String leagueid, String weekid) {
//        return new ObservableResult<Map<String, Map<String, PickView>>>() {
//            @Override
//            public Map<String, Map<String, PickView>> invoke() {
//            	
////            	UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
//            	log.debug("leagueid="+leagueid+" weekid="+weekid);
//            	ParameterizedTypeReference<Map<String, Map<String, PickView>>> responseType = new ParameterizedTypeReference<Map<String, Map<String, PickView>>>() {};
//                return secureRestTemplate.exchange("http://pick/picks/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
////            	return Collections.emptyMap();                
//            }
//        };
//    }
//
//    @SuppressWarnings("unused")
//    private Map<String, Map<String, PickView>> stubAllPicks(final String leagueId, final String weekId) {
//
//
//    	throw new RuntimeException();
//    }
    
//    @HystrixCommand(fallbackMethod = "stubAllDoublePick",
//            commandProperties = {
//                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
//            }
//    )
//    public Observable<Map<String, DoublePickView>> getAllDoublePickForPlayerForWeek(String leagueid, String weekid) {
//        return new ObservableResult<Map<String, DoublePickView>>() {
//            @Override
//            public Map<String, DoublePickView> invoke() {
//            	
//            	log.debug("leagueid="+leagueid+" weekid="+weekid);
//            	ParameterizedTypeReference<Map<String, DoublePickView>> responseType = new ParameterizedTypeReference<Map<String, DoublePickView>>() {};
//            	
//            	return secureRestTemplate.exchange("http://pick/picks/doubles/leagueid/{leagueid}/weekid/{weekid}", HttpMethod.GET, null, responseType, leagueid, weekid).getBody();
////            	return Collections.emptyMap();                
//            }
//        };
//    }
//
//    @SuppressWarnings("unused")
//    private DoublePickView stubAllDoublePick(final String leagueId, final String weekId) {
//    	
//    	throw new RuntimeException();
//    }
}


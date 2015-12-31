package com.makeurpicks.pick;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Service
public class PickIntegrationService {

private Log log = LogFactory.getLog(PickIntegrationService.class);


	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;

    
    
    public PickView createPick(PickView pickView)
    {
    	return secureRestTemplate.postForEntity("http://pick/picks/admin", pickView, PickView.class).getBody();
    }
    
    public void createDoublePick(DoublePickView doublePickView)
    {
//    	HttpEntity<DoublePickView> requestEntity = new HttpEntity<DoublePickView>(doublePickView);
//    	return secureRestTemplate.exchange("http://pick/double/", HttpMethod.PUT, requestEntity, DoublePickView.class).getBody();
    	secureRestTemplate.put("http://pick/picks/double", doublePickView);
    }
}

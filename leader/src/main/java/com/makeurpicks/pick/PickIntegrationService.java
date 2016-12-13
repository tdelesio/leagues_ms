package com.makeurpicks.pick;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.stereotype.Service;

@Service
public class PickIntegrationService {

	private Log log = LogFactory.getLog(PickIntegrationService.class);
	
	@Autowired
    @LoadBalanced
    private OAuth2RestOperations secureRestTemplate;
	
	
    
    
}

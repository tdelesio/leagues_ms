package com.makeurpicks.config;

import java.util.Map;

import org.springframework.cloud.cloudfoundry.CloudFoundryServiceInfoCreator;
import org.springframework.cloud.cloudfoundry.Tags;

public class URIWebServiceInfoCreator extends CloudFoundryServiceInfoCreator<WebServiceInfo> {

	 public URIWebServiceInfoCreator() {
	        super(new Tags(), "");
	    }
	 
	@Override
	public WebServiceInfo createServiceInfo(Map<String, Object> serviceData) {
		
		 String id = (String) serviceData.get("name");

	        Map<String, Object> credentials = getCredentials(serviceData);
	        String uri = getUriFromCredentials(credentials);

	        return new WebServiceInfo(id, uri);
	}

	
}

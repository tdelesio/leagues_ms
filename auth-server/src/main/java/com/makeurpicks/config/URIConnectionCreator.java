package com.makeurpicks.config;

import org.springframework.cloud.service.AbstractServiceConnectorCreator;
import org.springframework.cloud.service.ServiceConnectorConfig;

public class URIConnectionCreator extends AbstractServiceConnectorCreator<String, WebServiceInfo> {

	@Override
	public String create(WebServiceInfo serviceInfo, ServiceConnectorConfig serviceConnectorConfig) {
		return serviceInfo.getUri();
	}

}

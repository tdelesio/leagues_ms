package com.makeurpicks.config;

import javax.sql.DataSource;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("cloud")
public class CloudConfig extends AbstractCloudConfig {

	@Bean
	public DataSource datasource() {
		return cloud().getSingletonServiceConnector(DataSource.class, null);
				
//				RedisServiceInfo serviceInfo = (RedisServiceInfo) cloud.getServiceInfo("redis-myp");
//        String serviceID = serviceInfo.getId();
//        return cloud.getServiceConnector(serviceID, RedisConnectionFactory.class, null);
	}
}

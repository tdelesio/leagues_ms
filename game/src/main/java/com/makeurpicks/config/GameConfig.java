package com.makeurpicks.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GameConfig {

	@LoadBalanced
	@Bean
	RestTemplate restTemplate()
	{
		return new RestTemplate();
	}
}

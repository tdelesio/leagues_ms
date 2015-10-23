package com.makeurpicks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
//@EnableOAuth2Resource
//@ConfigurationProperties("spring.cloud.config.server")
public class LeaderApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(LeaderApplication.class, args);
	}

	
//	@Bean
//	public LeagueService leagueService()
//	{
//		LeagueService leagueService = new LeagueService();
//		return leagueService;
//	}
	
	
	
	
}

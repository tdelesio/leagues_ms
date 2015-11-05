package com.makeurpicks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@RestController
@EnableRedisHttpSession
public class AdminApplication {//extends WebSecurityConfigurerAdapter {

	public static void main(String[] args) {
    	new SpringApplicationBuilder(AdminApplication.class).web(true).run(args);
    }
	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// We need this to prevent the browser from popping up a dialog on a 401
//		http.httpBasic().disable();
//		http.authorizeRequests().anyRequest().authenticated();		
//
//	}

}

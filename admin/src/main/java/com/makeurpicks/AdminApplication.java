package com.makeurpicks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
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
public class AdminApplication {

	public static void main(String[] args) {
    	new SpringApplicationBuilder(AdminApplication.class).web(true).run(args);
    }

}

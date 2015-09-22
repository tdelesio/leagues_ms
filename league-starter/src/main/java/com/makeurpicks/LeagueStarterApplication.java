package com.makeurpicks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
public class LeagueStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeagueStarterApplication.class, args);
    }
}

package com.makeurpicks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.stream.EnableTurbineStream;

@SpringBootApplication
@EnableTurbineStream
@EnableDiscoveryClient
public class ServerTurbineApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerTurbineApplication.class, args);
    }
}

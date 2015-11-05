package com.makeurpicks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@Configuration
@ComponentScan
@EnableAutoConfiguration//(exclude={DataSourceAutoConfiguration.class})
@EnableEurekaClient
@EnableFeignClients
//@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableJpaRepositories
@EnableRedisHttpSession
public class GameApplication {//extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(GameApplication.class, args);
    }
    
//    @Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// We need this to prevent the browser from popping up a dialog on a 401
//		http.httpBasic().disable();
//		http.authorizeRequests().antMatchers(HttpMethod.POST, "/**").hasRole("WRITER").anyRequest().authenticated();
//	}
}

package com.makeurpicks;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
//@EnableZuulProxy
@RestController
@EnableRedisHttpSession
public class AdminApplication {

	public static void main(String[] args) {
    	new SpringApplicationBuilder(AdminApplication.class).web(true).run(args);
    }
	  
//	@Configuration
//	@EnableOAuth2Sso
//	@EnableAutoConfiguration
//	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter  {
// 
//	  @Override
//		public void configure(HttpSecurity http) throws Exception {
//			http.logout().and().antMatcher("/**").authorizeRequests()
//					.antMatchers("/index.html", "/home.html", "/", "/login", "/env", "/beans").permitAll()
//					.anyRequest().authenticated().and().csrf()
//					.csrfTokenRepository(csrfTokenRepository()).and()
//					.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
//		}
//	
//	private CsrfTokenRepository csrfTokenRepository() {
//		  HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//		  repository.setHeaderName("X-XSRF-TOKEN");
//		  return repository;
//		}
//	}
	
	@Configuration
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
				.httpBasic()
			.and()
				.authorizeRequests()
					.antMatchers("/index.html", "/unauthenticated.html", "/").permitAll()
					.anyRequest().hasRole("ADMIN")
			.and()
				.csrf().disable();
			// @formatter:on
		}
	}
	
	@RequestMapping("/user")
	public Map<String, Object> user(Principal user) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("name", user.getName());
		map.put("roles", AuthorityUtils.authorityListToSet(((Authentication) user)
				.getAuthorities()));
		return map;
	}
}

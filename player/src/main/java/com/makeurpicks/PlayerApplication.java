package com.makeurpicks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

@SpringBootApplication
//@RestController
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableCircuitBreaker
@ComponentScan
//@EnableResourceServer
//@EnableOAuth2Sso
public class PlayerApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerApplication.class);
	
//	@Autowired
//	private PlayerService playerService;
	
    public static void main(String[] args) {
        SpringApplication.run(PlayerApplication.class, args);
    }
    
//    @Bean
//    public ResourceServerConfigurer resourceServer(SecurityProperties securityProperties) {
//        return new ResourceServerConfigurerAdapter() {
//
//            @Override
//            public void configure(HttpSecurity http) throws Exception {
//                if (securityProperties.isRequireSsl()) {
//                    http.requiresChannel().anyRequest().requiresSecure();
//                }
//                http.authorizeRequests()
//                        .antMatchers("/players/userinfo/**").access("#oauth2.hasScope('openid')");
//            }
//        };
//    }
    
//    @Bean
//    public OAuth2SsoConfigurerAdapter oAuth2SsoConfigurerAdapter(SecurityProperties securityProperties) {
//        return new OAuth2SsoConfigurerAdapter() {
//            @Override
//            public void match(RequestMatchers matchers) {
////                matchers.antMatchers("/userinfo");
//                matchers.antMatchers("/**").authorizeRequests().anyRequest().permitAll();
//            }
//
//            @Override
//            public void configure(HttpSecurity http) throws Exception {
//                if (securityProperties.isRequireSsl()) {
//                    http.requiresChannel().anyRequest().requiresSecure();
//                }
//                http.authorizeRequests().anyRequest().authenticated();
//            }
//        };
//    }
    
    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

      @Autowired
      private AuthenticationManager authenticationManager;
      
      @Override
      public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
      }
      
      @Override
      public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            .withClient("acme")
            .secret("acmesecret")
            .authorizedGrantTypes("authorization_code", "refresh_token",
                "password").scopes("openid");
      }

  }
//    
//    @RequestMapping("/user")
//	public Principal user(Principal user) {
//		return user;
//	}
   
//    @RequestMapping(value = "/{id}", method=RequestMethod.GET ,produces = "application/json")
//    public @ResponseBody Player getPlayerById(@PathVariable String id)
////    		, @RequestHeader(value="Authorization") String authorizationHeader, Principal currentUser)
//    {
////    	LOG.info("ProductApi: User={}, Auth={}, called with productId={}", currentUser.getName(), authorizationHeader, id);
//    	
//    	return playerService.getPlayer(id);
//    }
    
  
}

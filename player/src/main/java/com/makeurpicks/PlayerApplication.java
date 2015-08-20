package com.makeurpicks;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Player;
import com.makeurpicks.service.PlayerService;

@SpringBootApplication
@RestController
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableCircuitBreaker
@EnableResourceServer
public class PlayerApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerApplication.class);
	
	@Autowired
	private PlayerService playerService;
	
    public static void main(String[] args) {
        SpringApplication.run(PlayerApplication.class, args);
    }
    
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
    
    @RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
   
    @RequestMapping(value = "/{id}", method=RequestMethod.GET ,produces = "application/json")
    public @ResponseBody Player getPlayerById(@PathVariable String id)
//    		, @RequestHeader(value="Authorization") String authorizationHeader, Principal currentUser)
    {
//    	LOG.info("ProductApi: User={}, Auth={}, called with productId={}", currentUser.getName(), authorizationHeader, id);
    	
    	return playerService.getPlayer(id);
    }
    
  
}

package com.makeurpicks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@RestController
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableCircuitBreaker
@ComponentScan
//@EnableResourceServer
public class PlayerApplication {

	private static final Logger LOG = LoggerFactory.getLogger(PlayerApplication.class);
	
//	@Autowired
//	private PlayerService playerService;
	
    public static void main(String[] args) {
        SpringApplication.run(PlayerApplication.class, args);
    }
    
//    @Configuration
//    @EnableAuthorizationServer
//    protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {
//
//      @Autowired
//      private AuthenticationManager authenticationManager;
//      
//      @Override
//      public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.authenticationManager(authenticationManager);
//      }
//      
//      @Override
//      public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        clients.inMemory()
//            .withClient("acme")
//            .secret("acmesecret")
//            .authorizedGrantTypes("authorization_code", "refresh_token",
//                "password").scopes("openid");
//      }
//
//  }
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

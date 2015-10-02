package com.makeurpicks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


//@Configuration
//@EnableAuthorizationServer
public class OAuth2Config
//extends WebSecurityConfigurerAdapter {
//	@Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/**").authorizeRequests().anyRequest().permitAll();
//    }
//	
{
//extends AuthorizationServerConfigurerAdapter {
//
//	@Autowired
//	private AuthenticationManager authenticationManager;
//	
//	@Override
//	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//		endpoints.authenticationManager(authenticationManager);
//	}
//	
//	@Override
//	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//		clients.inMemory()
//			.withClient("acme")
//			.secret("acmesecret")
//			.authorizedGrantTypes("authorization_code", "refresh_token", "implicit", "password", "client_credentials")
//			.scopes("webshop");
//	}
}

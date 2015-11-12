package com.makeurpicks;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.util.WebUtils;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@EnableZuulProxy
public class AdminApplication {

	public static void main(String[] args) {
//    	new SpringApplicationBuilder(AdminApplication.class).web(true).run(args);
    	SpringApplication.run(AdminApplication.class, args);
    }
	
//	@Value("${config.oauth2.accessTokenUri}")
//    private String accessTokenUri;
//
//    @Value("${config.oauth2.userAuthorizationUri}")
//    private String userAuthorizationUri;
//
//    @Value("${config.oauth2.clientID}")
//    private String clientID;
//
//    @Value("${config.oauth2.clientSecret}")
//    private String clientSecret;
    
//    @Bean
//    public OAuth2RestOperations restTemplate(OAuth2ClientContext oauth2ClientContext) {
//        return new OAuth2RestTemplate(resource(), oauth2ClientContext);
//    }
//    
//	@Bean
//	public OAuth2ProtectedResourceDetails resource() {
//        AuthorizationCodeResourceDetails resource = new AuthorizationCodeResourceDetails();
//        resource.setClientId(clientID);
//        resource.setClientSecret(clientSecret);
//        resource.setAccessTokenUri(accessTokenUri);
//        resource.setUserAuthorizationUri(userAuthorizationUri);
//        resource.setScope(Arrays.asList("read"));
//
//        return resource;
//    }
	
//	@RequestMapping(method=RequestMethod.GET, value="/role")
//	public @ResponseBody Object getGame(Principal user)
//	{
//		
//		;
////		System.out.println(user);
//		
////		 OAuth2Authentication authentication = (OAuth2Authentication) user;
////		 System.out.println(authentication.getUserAuthentication().getDetails());
////		return authentication.getUserAuthentication().getDetails();
//	}
	
	@Configuration
	@EnableOAuth2Sso 
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//        @Override
//        public void match(OAuth2SsoConfigurer.RequestMatchers matchers) {
//            matchers.anyRequest();
//        }
		
		
		

		
        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
            	.logout().and()
//            	.httpBasic().disable()
            	
//            	.antMatcher("/**")
            	.authorizeRequests()
                    .antMatchers("/login", "/beans", "/user").permitAll()
//                    .antMatchers(HttpMethod.GET, "/recommendations/**","/reviews/**","/people/**","/movie/**","/catalog/**","/likes/**").permitAll()
//                    .anyRequest().authenticated().and()
//                    .antMatchers("/**").hasRole("ADMIN")
                    .anyRequest().authenticated().and()
//                .formLogin()
//	                .loginPage("/login")
//	                .loginProcessingUrl("http://localhost:9999/uaa/login")
////	                .successHandler(successHandler())
//	                .permitAll()
//	                .and()
                .csrf()
                    .csrfTokenRepository(csrfTokenRepository()).and()
                    .addFilterBefore(new RequestContextFilter(), HeaderWriterFilter.class)
                    .addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
        }

        private Filter csrfHeaderFilter() {
            return new OncePerRequestFilter() {
                @Override
                protected void doFilterInternal(HttpServletRequest request,
                                                HttpServletResponse response, FilterChain filterChain)
                        throws ServletException, IOException {
                    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                            .getName());
                    if (csrf != null) {
                        Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
                        String token = csrf.getToken();
                        if (cookie == null || token != null
                                && !token.equals(cookie.getValue())) {
                            cookie = new Cookie("XSRF-TOKEN", token);
                            cookie.setPath("/");
                            response.addCookie(cookie);
                        }
                    }
                    filterChain.doFilter(request, response);
                }
            };
        }

        private CsrfTokenRepository csrfTokenRepository() {
            HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
            repository.setHeaderName("X-XSRF-TOKEN");
            return repository;
        }

    }
}

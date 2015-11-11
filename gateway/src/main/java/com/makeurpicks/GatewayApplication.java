package com.makeurpicks;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.filter.RequestContextFilter;
import org.springframework.web.util.WebUtils;

//@EnableRedisHttpSession
@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
@EnableOAuth2Sso
//@RestController
public class GatewayApplication {


	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	@Configuration
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//        @Override
//        public void match(OAuth2SsoConfigurer.RequestMatchers matchers) {
//            matchers.anyRequest();
//        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.logout().and()
            	.httpBasic().disable()
            	
            	.antMatcher("/**").authorizeRequests()
                    .antMatchers("/login", "/beans").permitAll()
                    .antMatchers(HttpMethod.GET, "/recommendations/**","/reviews/**","/people/**","/movie/**","/catalog/**","/likes/**").permitAll()
                    .anyRequest().authenticated().and()
                    .formLogin()
//	                .loginPage("/login.html")
//	                .loginProcessingUrl("/login")
////	                .successHandler(successHandler())
	                .permitAll()
	                .and()
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
	
//	@Configuration
//	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
//	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
//		
//		@Bean
//		public AuthenticationSuccessHandler successHandler() {
//		    SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
//		    handler.setUseReferer(true);
//		    return handler;
//		}
//		
//		@Override
//		protected void configure(HttpSecurity http) throws Exception {
//			 http
////			 	.httpBasic()
////			 		.and()
//	            .authorizeRequests()
//	                .antMatchers("/", "/home").permitAll()
//	                .anyRequest().authenticated()
//	                .and()
//	            .formLogin()
//	                .loginPage("/login.html")
//	                .loginProcessingUrl("/login")
//	                .successHandler(successHandler())
//	                .permitAll()
//	                .and()
//	            .logout()
//	                .permitAll()
//	                .and()
//				.csrf()
////					.disable();
//					.csrfTokenRepository(csrfTokenRepository())
//					.and()
//				.addFilterAfter(csrfHeaderFilter(), SessionManagementFilter.class);
////			 	.addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
//		}
//		
//		
//		@Autowired
//		public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
//			// @formatter:off	
//			auth.inMemoryAuthentication()
//				.withUser("user").password("password").roles("USER")
//			.and()
//				.withUser("admin").password("admin").roles("USER", "ADMIN", "READER", "WRITER")
//			.and()
//				.withUser("audit").password("audit").roles("USER", "ADMIN", "READER");
//// @formatter:on
//		}
//		
////		@Autowired
////	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
////	        auth
////	            .inMemoryAuthentication()
////	                .withUser("user").password("password").roles("USER");
////	    }
//		
//
//		private Filter csrfHeaderFilter() {
//			return new OncePerRequestFilter() {
//				@Override
//				protected void doFilterInternal(HttpServletRequest request,
//						HttpServletResponse response, FilterChain filterChain)
//						throws ServletException, IOException {
//
//					CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
//							.getName());
//					if (csrf != null) {
//						Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
//						String token = csrf.getToken();
//						if (cookie == null || token != null
//								&& !token.equals(cookie.getValue())) {
//							cookie = new Cookie("XSRF-TOKEN", token);
//							cookie.setPath("/");
//							response.addCookie(cookie);
//						}
//					}
//					filterChain.doFilter(request, response);
//				}
//			};
//		}
//
//		private CsrfTokenRepository csrfTokenRepository() {
//			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
//			repository.setHeaderName("X-XSRF-TOKEN");
//			return repository;
//		}
//	}

}
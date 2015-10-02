package com.makeurpicks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.security.oauth2.sso.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
//@EnableResourceServer
public class ZuulApplication {

    public static void main(String[] args) {
    	new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
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
////                		.antMatchers("/admin/**")
////                			.access("#oauth2.hasScope('admin')")
//                        .antMatchers("/**")
////                        .access("#oauth2.hasScope('admin')");
//                        	.access("#oauth2.hasScope('openid')");
//            }
//        };
//    }
    
    
}

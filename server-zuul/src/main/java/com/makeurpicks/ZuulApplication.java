package com.makeurpicks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.stereotype.Controller;

@SpringBootApplication
@EnableZuulProxy
@Controller
public class ZuulApplication {

    public static void main(String[] args) {
    	new SpringApplicationBuilder(ZuulApplication.class).web(true).run(args);
    }
}

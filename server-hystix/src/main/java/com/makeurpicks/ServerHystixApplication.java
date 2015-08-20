package com.makeurpicks;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@Controller
@EnableHystrixDashboard
public class ServerHystixApplication extends SpringBootServletInitializer{

	@RequestMapping("/")
	public String home() {
		return "forward:/hystrix";
	}

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ServerHystixApplication.class).web(true);
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ServerHystixApplication.class).web(true).run(args);
    }
}

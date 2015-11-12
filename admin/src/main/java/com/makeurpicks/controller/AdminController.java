package com.makeurpicks.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

//	@RequestMapping("/user")
//	@ResponseBody
//	public Principal user(Principal user) {
//		return user;
//	}
	
	@Autowired
    private OAuth2RestOperations restTemplate;

    @Value("${config.oauth2.resourceURI}")
    private String resourceURI;
	
	@RequestMapping("/user")
    public Object home(Principal principal) {
//        return restTemplate.getForObject(resourceURI, Object.class);
		return principal;
    }
	
	@RequestMapping("/resource")
    public Object resource() {
        return restTemplate.getForObject(resourceURI, Object.class);
//		return principal;
    }

}


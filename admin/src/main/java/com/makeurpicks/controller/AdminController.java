package com.makeurpicks.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.service.AdminService;
import com.makeurpicks.service.Dummy;

@RestController
public class AdminController {

//	@Autowired
//    private OAuth2RestOperations restTemplate;
//
//    @Value("${config.oauth2.resourceURI}")
//    private String resourceURI;
	
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping("/user")
    public Object home(Principal principal) {
//        return restTemplate.getForObject(resourceURI, Object.class);
		return principal;
    }
	

	@RequestMapping("/dummy")
	public @ResponseBody Dummy createDummyData()
	{
		return adminService.createDummyWeeks();
	}
}


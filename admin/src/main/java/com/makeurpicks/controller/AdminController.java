package com.makeurpicks.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@RestController
public class AdminController {

//	@Autowired
//	private LeagueClient leagueClient;
//	
//	@Autowired
//	private GameClient gameClient;
	
	@RequestMapping("/token")
	  @ResponseBody
	  public Map<String,String> token(HttpSession session) {
	    return Collections.singletonMap("token", session.getId());
	  }
	 

	 @RequestMapping("/user")
	  public Principal user(Principal user) {
	    return user;
	  }
	 
	 @RequestMapping("/csrf")
	 public CsrfToken csrf(CsrfToken token)
	 {
		 return token;
	 }
}

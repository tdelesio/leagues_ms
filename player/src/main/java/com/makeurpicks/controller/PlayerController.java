package com.makeurpicks.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.makeurpicks.domain.Player;
import com.makeurpicks.domain.User;
import com.makeurpicks.service.PlayerService;

@RestController
@RequestMapping(value="/players")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
//	@Autowired
//    private ResourceServerProperties resourceServerProperties;

    @Autowired
    private OAuth2RestTemplate oauth2RestTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
	
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public @ResponseBody Player login(@RequestBody User user)
	{
		return playerService.login(user);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/password")
	public @ResponseBody User updatePassword(@RequestBody User user)
	{
		return playerService.updatePassword(user);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/password")
	public @ResponseBody boolean initiateUpdatePasswordRequest(@RequestBody User user)
	{
		playerService.initiateUpdatePasswordRequest(user);
		return true;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/{id}")
	public @ResponseBody Player getPlayerById(@PathVariable String id)
	{
		return playerService.getPlayer(id);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/username/{username}")
	public @ResponseBody Player getPlayerByUserName(@PathVariable String username)
	{
		return playerService.getPlayerByUserName(username);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Player register(@RequestBody Player player)
	{
		return playerService.register(player);
	}
	
	 @RequestMapping("/userinfo")
	    public String userinfo(Model model, Principal principal) throws Exception {
//	        Map<?, ?> userInfoResponse = oauth2RestTemplate.getForObject(resourceServerProperties.getUserInfoUri(),
//	                Map.class);
//	        model.addAttribute("username", principal.getName());
//	        model.addAttribute("response",
//	                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(userInfoResponse));
//	        model.addAttribute("token", oauth2RestTemplate.getOAuth2ClientContext().getAccessToken().getValue());
//	        return "userinfo";
		 return principal.getName();
	    }
}

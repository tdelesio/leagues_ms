package com.makeurpicks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Player;
import com.makeurpicks.domain.User;
import com.makeurpicks.service.PlayerService;

@RestController
@RequestMapping(value="/player", produces = "application/json", consumes = "application/json")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	@RequestMapping(method=RequestMethod.POST, value="/login")
	public @ResponseBody User login(@RequestBody User user)
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
}

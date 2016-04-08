package com.makeurpicks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.Player;
import com.makeurpicks.service.PlayerService;

@RestController
@RequestMapping("/players")
public class PlayerController {

	@Autowired
	private PlayerService playerService;
	
	@Value("${config.oauth2.ui-uri}")
	private String ui;
	
    @Value("${config.oauth2.admin-uri}")
	private String admin;
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Player createPlayer(@RequestBody Player player) {

			return playerService.createPlayer(player);
	
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/")
	public String testOpen()
	{
		return ui;
	}
	
}

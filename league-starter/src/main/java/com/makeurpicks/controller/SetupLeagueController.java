package com.makeurpicks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.View;
import com.makeurpicks.service.SetupLeagueService;

@RestController
public class SetupLeagueController {

	@Autowired
	private SetupLeagueService setupLeagueService;
	
	@RequestMapping(value="/setup")
	public @ResponseBody View setupLeague() {
		return setupLeagueService.setupLeague();
		
	}
}

package com.makeurpicks.service;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.makeurpicks.domain.Player;

public interface PlayerClient {

	@RequestMapping(method=RequestMethod.POST, value="/player")
	public @ResponseBody Player register(@RequestBody Player player);
}

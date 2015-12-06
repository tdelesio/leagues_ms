package com.makeurpicks.controller;

import java.security.Principal;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.game.GameIntegrationService;
import com.makeurpicks.service.PickService;

@RestController
@RequestMapping(value="/picks")
public class PickController  {

	private Log log = LogFactory.getLog(PickController.class);
	
	@Autowired
	private PickService pickService;
	
	
	@RequestMapping(method=RequestMethod.GET, value="/weekid/{weekid}")
	public @ResponseBody Map<String, Map<String, Pick>> getPicksByWeek(@PathVariable String weekid)
	{
		return pickService.getPicksByWeek(weekid);
	}

	@RequestMapping(method=RequestMethod.GET, value="/self/weekid/{weekid}")
	public @ResponseBody Map<String, Pick> getPicksByWeekAndPlayer(Principal user, @PathVariable String weekid)
	{
		return pickService.getPicksByWeekAndPlayer(weekid, user.getName());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/player/weekid/{weekid}/playerid/{playerid}")
	public @ResponseBody Map<String, Pick> getPicksByWeekAndPlayer(Principal user, @PathVariable String weekid, @PathVariable String playerid)
	{
		return pickService.getOtherPicksByWeekAndPlayer(weekid, playerid);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Pick makePick(Principal user, @RequestBody Pick pick)
	{
		pick.setPlayerId(user.getName());
		
		log.debug(pick);
		
		return pickService.makePick(pick);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/")
	public @ResponseBody Pick updatePick(Principal user, @RequestBody Pick pick)
	{
		pick.setPlayerId(user.getName());
		
		log.debug(pick);
		
		return pickService.updatePick(pick);
	}
	
	
	@RequestMapping(method=RequestMethod.PUT, value="/double")
	public @ResponseBody DoublePick makeDoublePick(Principal user, @RequestBody DoublePick pick)
	{
		log.debug(pick);
		
		return pickService.makeDoublePick(pick.getPickId(), user.getName());
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/double/weekid/{weekid}")
	public @ResponseBody DoublePick getDoublePick(Principal user, @PathVariable String weekid)
	{
		return pickService.getDoublePick(weekid, user.getName());
	}
	

}
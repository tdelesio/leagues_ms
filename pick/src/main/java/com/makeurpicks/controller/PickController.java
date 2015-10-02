package com.makeurpicks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.service.PickService;

@RestController
@RequestMapping(value="/picks")
public class PickController {

	@Autowired
	private PickService pickService;
	
	@RequestMapping(method=RequestMethod.GET, value="/leagueid/{leagueid}/weekid/{weekid}")
	public @ResponseBody Iterable<Pick> getPicksBySeasonAndWeek(@PathVariable String leagueid, @PathVariable String weekid)
	{
		return pickService.getPicksByLeagueAndWeek(leagueid, weekid);
	}

	@RequestMapping(method=RequestMethod.GET, value="/leagueid/{leagueid}/weekid/{weekid}/playerid/{playerid}")
	public @ResponseBody Iterable<Pick> getPicksByLeaguenWeekAndPlayer(@PathVariable String leagueid, @PathVariable String weekid, @PathVariable String playerid)
	{
		return pickService.getPicksByLeagueWeekAndPlayer(leagueid, weekid, playerid);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody Pick makePick(@RequestBody Pick pick)
	{
		return pickService.makePick(pick);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/")
	public @ResponseBody Pick updatePick(@RequestBody Pick pick)
	{
		return pickService.updatePick(pick, pick.getPlayerId());
	}
	
	
	@RequestMapping(method=RequestMethod.PUT, value="/double")
	public @ResponseBody DoublePick makeDoublePick(@RequestBody DoublePick pick)
	{
		return pickService.makeDoublePick(pick.getPickId(), "");
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/double/leagueid/{leagueid}/weekid/{weekid}/playerid/{playerid}")
	public @ResponseBody DoublePick getDoublePick(@PathVariable String leagueid, @PathVariable String weekid, @PathVariable String playerid)
	{
		return pickService.getDoublePick(leagueid, weekid, playerid);
	}
}
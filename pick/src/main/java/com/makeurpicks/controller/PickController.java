package com.makeurpicks.controller;

import java.util.List;

import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.service.PickService;

@RestController
@RequestMapping(produces = "application/json", consumes = "application/json")
public class PickController {

	@Autowired
	private PickService pickService;
	
	//Path("/picks/leagueid/{leagueid}/weekid/{weekid}")
	@RequestMapping(method=RequestMethod.GET, value="/pick/leagueid/{leagueId}/weekid/{weekId}")
	public @ResponseBody Iterable<Pick> getPicksByLeagueAndWeek(@PathParam("leagueId")String leagueid, @PathParam("id")String weekId)
	{
		return pickService.getPicksByLeagueAndWeek(leagueid, weekId);
	}



//	@Path("/picks/leagueid/{leagueid}/weekid/{weekid}/player/{playerid}")
	@RequestMapping(method=RequestMethod.GET, value="/pick/leagueid/{leagueid}/weekid/{weekid}/player/{playerid}")
	public @ResponseBody Iterable<Pick> getPicksByLeagueWeekAndPlayer(@PathParam("leagueid")String leagueid, @PathParam("weekid")String weekid, @PathParam("playerid")String playerid)
	{
		return pickService.getPicksByLeagueWeekAndPlayer(leagueid, weekid, playerid);
	}
	
	
//	@Path("/pick")
	@RequestMapping(method=RequestMethod.POST, value="/pick")
	public @ResponseBody Pick makePick(Pick pick)
	{
		return pickService.makePick(pick);
	}
	
//	@Path("/pick")
	@RequestMapping(method=RequestMethod.PUT, value="/pick")
	public @ResponseBody Pick updatePick(Pick pick)
	{
		return pickService.updatePick(pick, pick.getPlayerId());
	}
	
	
//	@Path("/double/{pickid}")
	@RequestMapping(method=RequestMethod.PUT, value="/pick/double")
	public @ResponseBody DoublePick makeDoublePick(DoublePick pick)
	{
		return pickService.makeDoublePick(pick.getPickId(), "");
	}
}
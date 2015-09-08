package com.makeurpicks.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.PlayerLeague;
import com.makeurpicks.domain.Season;
import com.makeurpicks.service.LeagueService;

@RestController
@RequestMapping(value="/league", produces = MediaType.APPLICATION_JSON, consumes = {MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
public class LeagueController {

	@Autowired
	private LeagueService leagueService;
	
//	 @Value("${greeting}")
	 private String greeting;
	 
	 @RequestMapping(method=RequestMethod.GET, value="/")
		public @ResponseBody Iterable<League> getAllLeague() {

				return leagueService.getAllLeagues();
		
		}
	 
	 @RequestMapping(method=RequestMethod.GET, value="/{id}",produces = MediaType.APPLICATION_JSON)
	 public @ResponseBody League getLeagueById(@PathVariable("id")String id)
	 {
		return leagueService.getLeagueById(id);
	 }
	 
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody League createLeague(@RequestBody League league) {

			return leagueService.createLeague(league);
	
	}

	@RequestMapping(method=RequestMethod.PUT, value="/")
	 public @ResponseBody League updateLeague(League league)
	 {
		return leagueService.updateLeague(league);
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/player/{id}")
	public @ResponseBody List<League> getLeaguesForPlayer(@PathVariable("id")String id)
	{
		return leagueService.getLeaguesForPlayer(id);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/player")
	public void addPlayerToLeague(PlayerLeague playerLeague)
	{
		leagueService.joinLeague(playerLeague.getLeagueId(), playerLeague.getPlayerId(), playerLeague.getPassword());
	
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/name/{name}",produces = MediaType.APPLICATION_JSON)
	 public @ResponseBody League getLeagueByName(@PathVariable String name)
	 {
		return leagueService.getLeagueByName(name);
	 }
	
	@RequestMapping(method=RequestMethod.DELETE, value="/league/player")
	 public void removePlayerFromLeagye(PlayerLeague playerLeague)
	 {
		leagueService.removePlayerFromLeagye(playerLeague.getLeagueId(), playerLeague.getPlayerId());
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/season/current")
	public @ResponseBody List<Season> getCurrentSeasons()
	{
		return leagueService.getCurrentSeasons();
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/season")
	public @ResponseBody Season createSeason(@RequestBody Season season)
	{
		return leagueService.createSeason(season);
	}
	
	@RequestMapping(method=RequestMethod.PUT, value="/season")
	public @ResponseBody Season updateSeason(@RequestBody Season season)
	{
		return leagueService.updateSeason(season);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/leaguetype")
	public @ResponseBody LeagueType[] getLeagueType()
	{
		return leagueService.getLeagueType();
	}
}

package com.makeurpicks.controller;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.PlayerLeague;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.service.LeagueService;

@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON, consumes = {MediaType.APPLICATION_JSON,MediaType.TEXT_PLAIN})
public class LeagueController {

	@Autowired
	private LeagueService leagueService;
	
	 @Value("${greeting}")
	 private String greeting;
	 
	 @RequestMapping(method=RequestMethod.GET, value="/")
		public @ResponseBody Iterable<League> getAllLeague() {

				return leagueService.getAllLeagues();
		
		}
	 
	 @RequestMapping(method=RequestMethod.GET, value="/{id}",produces = MediaType.APPLICATION_JSON)
	 public @ResponseBody League getLeagueById(@PathParam("id")String leagueId)
	 {
		League league =  new League();
		league.setLeagueName(greeting);
		return league;
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
	public @ResponseBody List<League> getLeaguesForPlayer(@PathParam("id")String playerId)
	{
		return leagueService.getLeaguesForPlayer(playerId);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/player")
	public void addPlayerToLeague(PlayerLeague playerLeague)
	{
		leagueService.joinLeague(playerLeague.getLeagueId(), playerLeague.getPlayerId(), playerLeague.getPassword());
	
	}
	
	
	
	
//	 public League getLeagueByName(String name)
//	 {
//	
//	 }
	
	@RequestMapping(method=RequestMethod.DELETE, value="/league/player")
	 public void removePlayerFromLeagye(PlayerLeague playerLeague)
	 {
		leagueService.removePlayerFromLeagye(playerLeague.getLeagueId(), playerLeague.getPlayerId());
	 }
}

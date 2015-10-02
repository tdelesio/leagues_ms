package com.makeurpicks.controller;

import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.PlayerLeague;
import com.makeurpicks.domain.PlayerResponse;
import com.makeurpicks.domain.PlayersInLeague;
import com.makeurpicks.service.LeagueService;

@RestController
@RequestMapping(value="/leagues")
public class LeagueController {

	@Autowired
	private LeagueService leagueService;
	
	 @Value("${greeting}")
	 private String greeting;
	 
	 @RequestMapping(method=RequestMethod.GET, value="/")
		public @ResponseBody Iterable<League> getAllLeague() {

				return leagueService.getAllLeagues();
		
		}
	 
	 @RequestMapping(method=RequestMethod.GET, value="/{id}")
	 public @ResponseBody League getLeagueById(@PathVariable String id)
	 {
		return leagueService.getLeagueById(id);
	 }
	 
	@RequestMapping(method=RequestMethod.POST, value="/")
	public @ResponseBody League createLeague(@RequestBody League league) {

			return leagueService.createLeague(league);
	
	}

	@RequestMapping(method=RequestMethod.PUT, value="/")
	 public @ResponseBody League updateLeague(@RequestBody League league)
	 {
		return leagueService.updateLeague(league);
	 }
	
	
	@RequestMapping(method=RequestMethod.GET, value="/player/{id}")
	public @ResponseBody Set<LeagueName> getLeaguesForPlayer(@PathVariable String id)
	{ 
		return leagueService.getLeaguesForPlayer(id);
	}
	
	
	@RequestMapping(method=RequestMethod.POST, value="/player")
	public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague)
	{
		leagueService.joinLeague(playerLeague.getLeagueId(), playerLeague.getPlayerId(), playerLeague.getPassword());
	
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/name/{name}",produces = MediaType.APPLICATION_JSON)
	 public @ResponseBody League getLeagueByName(@PathVariable String name)
	 {
		return leagueService.getLeagueByName(name);
	 }
	
	@RequestMapping(method=RequestMethod.DELETE, value="/player")
	 public void removePlayerFromLeagye(@RequestBody PlayerLeague playerLeague)
	 {
		leagueService.removePlayerFromLeagye(playerLeague.getLeagueId(), playerLeague.getPlayerId());
	 }
	
	@RequestMapping(method=RequestMethod.GET, value="/player/leagueid/{leagueid}")
	 public @ResponseBody Set<PlayerResponse> getPlayersInLeague(@PathVariable String leagueid)
	 {
		return leagueService.getPlayersInLeague(leagueid);
	 }
	
	

}

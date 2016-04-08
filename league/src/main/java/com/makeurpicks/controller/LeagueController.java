package com.makeurpicks.controller;

import java.security.Principal;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.PlayerLeague;
import com.makeurpicks.service.LeagueService;

@RestController
@RequestMapping(value="/leagues")
public class LeagueController {

	private Log log = LogFactory.getLog(LeagueController.class);
	
	@Autowired
	private LeagueService leagueService;
	 
	@RequestMapping("/user")
	@PreAuthorize("hasRole('ADMIN')")
    public Principal resource(Principal principal) {
        return principal;
    }
	 
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
	public @ResponseBody League createLeague(Principal user, @RequestBody League league) {

		if (league != null && league.getAdminId() == null)
			league.setAdminId(user.getName());
		
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
	public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague, Principal principal)
	{
		playerLeague.setPlayerId(principal.getName());
		
		log.debug("playerLeague ="+ playerLeague.toString());
		
		leagueService.joinLeague(playerLeague);
	
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/player/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague)
	{
		log.debug("playerLeague ="+ playerLeague.toString());
		
		leagueService.joinLeague(playerLeague);
	
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
	 public @ResponseBody Set<String> getPlayersInLeague(@PathVariable String leagueid)
	 {
		return leagueService.getPlayersInLeague(leagueid);
	 }
	
	@RequestMapping(method=RequestMethod.DELETE, value="/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public @ResponseBody boolean deleteLeague(@PathVariable String id)
	{
		leagueService.deleteLeague(id);
		return true;
	}
	
	

}

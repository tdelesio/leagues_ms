package com.makeurpicks.controller;

import java.security.Principal;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
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
import com.makeurpicks.service.LeagueService;

@RestController
@EnableResourceServer
@RequestMapping(value="/leagues")
public class LeagueController extends ResourceServerConfigurerAdapter {

	@Autowired
	private LeagueService leagueService;
	 
	@Override
	public void configure(HttpSecurity http) throws Exception {
	        http
	        	.authorizeRequests()
	            	.antMatchers(HttpMethod.POST, "/**").authenticated()
	            	.and()
	            .authorizeRequests()
	            	.antMatchers(HttpMethod.PUT, "/**").authenticated()
	            	.and()
	            .authorizeRequests()
	            	.antMatchers(HttpMethod.DELETE, "/**").authenticated()
	            	.and()
	            .authorizeRequests()
	                .anyRequest().permitAll();
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

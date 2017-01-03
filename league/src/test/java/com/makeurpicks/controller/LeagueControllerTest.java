package com.makeurpicks.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueBuilder;
import com.makeurpicks.service.LeagueService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LeagueControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	private LeagueService leagueService;
	
	@Test
	public void getAllleaguesWithoutAccessTokenShouldRespondUnauthorized() throws Exception {
		this.mockMvc.perform(get("/leagues/").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnauthorized());
	}
	
	private League league1;
	private League league2;
	private League league3;
	
	private void stubData()
	{

			String player1Id = "1";
			String player2Id = "2";
			String player3Id = "3";
			String player4Id = "4";
			String player5Id = "5";
			
		league1 = new LeagueBuilder()
				.withAdminId(player1Id)
				.withName("pickem")
				.withPassword("football")
				.withSeasonId("1")
				.build();
				
				league2 = new LeagueBuilder()
				.withAdminId(player1Id)
				.withName("suicide")
				.withPassword("football")
				.withSeasonId("1")
				.build();
				
				league3 = new LeagueBuilder()
				.withAdminId(player1Id)
				.withName("superbowl")
				.withPassword("football")
				.withSeasonId("1")
				.build();
			
				List<League> allLeagues = new ArrayList<>();
				allLeagues.add(league1);
				allLeagues.add(league2);
				allLeagues.add(league3);
				
//				when(leagueService.getAllLeagues()).thenReturn(allLeagues);
//				when(leagueService.getLeagueByName(league1.getLeagueName())).thenReturn(league1);
//				when(leagueService.getLeagueByName(league2.getLeagueName())).thenReturn(league2);
//				when(leagueService.getLeagueByName(league3.getLeagueName())).thenReturn(league3);
				
	}
	 
	 
	 
	 
//	 @RequestMapping(method=RequestMethod.GET, value="/{id}")
//	 public @ResponseBody League getLeagueById(@PathVariable String id)
//	 {
//		return leagueService.getLeagueById(id);
//	 }
//	 
//	@RequestMapping(method=RequestMethod.POST, value="/")
//	public @ResponseBody League createLeague(Principal user, @RequestBody League league) {
//
//		if (league != null && league.getAdminId() == null)
//			league.setAdminId(user.getName());
//		
//		return leagueService.createLeague(league);
//	
//	}
//
//	@RequestMapping(method=RequestMethod.PUT, value="/")
//	 public @ResponseBody League updateLeague(@RequestBody League league)
//	 {
//		return leagueService.updateLeague(league);
//	 }
//	
//	
//	@RequestMapping(method=RequestMethod.GET, value="/player/{id}")
//	public @ResponseBody Set<LeagueName> getLeaguesForPlayer(@PathVariable String id)
//	{ 
//		return leagueService.getLeaguesForPlayer(id);
//	}
//	
//	@RequestMapping(method=RequestMethod.POST, value="/player")
//	public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague, Principal principal)
//	{
//		playerLeague.setPlayerId(principal.getName());
//		
//		log.debug("playerLeague ="+ playerLeague.toString());
//		
//		leagueService.joinLeague(playerLeague);
//	
//	}
//	
//	@RequestMapping(method=RequestMethod.POST, value="/player/admin")
//	@PreAuthorize("hasRole('ADMIN')")
//	public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague)
//	{
//		log.debug("playerLeague ="+ playerLeague.toString());
//		
//		leagueService.joinLeague(playerLeague);
//	
//	}
//	
//	@RequestMapping(method=RequestMethod.GET, value="/name/{name}",produces = MediaType.APPLICATION_JSON)
//	 public @ResponseBody League getLeagueByName(@PathVariable String name)
//	 {
//		return leagueService.getLeagueByName(name);
//	 }
//	
//	@RequestMapping(method=RequestMethod.DELETE, value="/player")
//	 public void removePlayerFromLeagye(@RequestBody PlayerLeague playerLeague)
//	 {
//		leagueService.removePlayerFromLeagye(playerLeague.getLeagueId(), playerLeague.getPlayerId());
//	 }
//	
//	@RequestMapping(method=RequestMethod.GET, value="/player/leagueid/{leagueid}")
//	 public @ResponseBody Set<String> getPlayersInLeague(@PathVariable String leagueid)
}

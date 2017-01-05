package com.makeurpicks.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueBuilder;
import com.makeurpicks.service.LeagueService;
import com.makeurpicks.test.rest.config.WithOAuth2Authentication;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class LeagueControllerTest {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	// we need two one MVC with out OAuthAuthentication, other with
	// OauthAuthentication
	@Autowired
	MockMvc mockMvc;
	private MockMvc mvc;
	@Autowired
	private WebApplicationContext webapp;

	@MockBean
	private LeagueService leagueService;

	private League league1;
	private League league2;
	private League league3;

	@Before
	public void setup() {
		String player1Id = "1";
		String player2Id = "2";
		String player3Id = "3";
		mvc = webAppContextSetup(webapp).build();
		league1 = new LeagueBuilder().withAdminId(player1Id).withName("pickem").withPassword("football")
				.withSeasonId("1").build();

		league2 = new LeagueBuilder().withAdminId(player2Id).withName("suicide").withPassword("football")
				.withSeasonId("1").build();

		league3 = new LeagueBuilder().withAdminId(player3Id).withName("superbowl").withPassword("football")
				.withSeasonId("1").build();

		List<League> allLeagues = new ArrayList<>();
		allLeagues.add(league1);
//		allLeagues.add(league2);
//		allLeagues.add(league3);
		given(this.leagueService.getAllLeagues()).willReturn(allLeagues);
		given(this.leagueService.getLeagueById("1")).willReturn(league1);
	}

	@Test
	public void getAllleaguesWithoutAccessTokenShouldRespondUnauthorized() throws Exception {
		this.mockMvc.perform(get("/").accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());
	}

	@Test
	@WithOAuth2Authentication
	public void getAllleaguesWithoutAuthenticationRespondStatusOk() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk());
		verify(this.leagueService).getAllLeagues();
	}
	
	@Test
	@WithOAuth2Authentication
	public void getLeaguesByIdgivenId1returnLeague() throws Exception {
		mvc.perform(get("/1")).andExpect(status().isOk()).andExpect(content().json("{'adminId':'1','leagueName':'pickem','password':'football','seasonId':'1'}"));
	}

	/*@Test
	@WithOAuth2Authentication(username="admin")
	public void createLeagueShouldCallCreateLeagueOnLeagueService() throws Exception {
		 ObjectMapper mapper = new ObjectMapper();
		    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		    String requestJson=ow.writeValueAsString(league2 );
		mvc.perform(post("/").contentType(APPLICATION_JSON_UTF8).content(requestJson));
		verify(this.leagueService).createLeague(league2);
	}*/
	
	// @RequestMapping(method=RequestMethod.POST, value="/")
	// public @ResponseBody League createLeague(Principal user, @RequestBody
	// League league) {
	//
	// if (league != null && league.getAdminId() == null)
	// league.setAdminId(user.getName());
	//
	// return leagueService.createLeague(league);
	//
	// }
	//
	// @RequestMapping(method=RequestMethod.PUT, value="/")
	// public @ResponseBody League updateLeague(@RequestBody League league)
	// {
	// return leagueService.updateLeague(league);
	// }
	//
	//
	// @RequestMapping(method=RequestMethod.GET, value="/player/{id}")
	// public @ResponseBody Set<LeagueName> getLeaguesForPlayer(@PathVariable
	// String id)
	// {
	// return leagueService.getLeaguesForPlayer(id);
	// }
	//
	// @RequestMapping(method=RequestMethod.POST, value="/player")
	// public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague,
	// Principal principal)
	// {
	// playerLeague.setPlayerId(principal.getName());
	//
	// log.debug("playerLeague ="+ playerLeague.toString());
	//
	// leagueService.joinLeague(playerLeague);
	//
	// }
	//
	// @RequestMapping(method=RequestMethod.POST, value="/player/admin")
	// @PreAuthorize("hasRole('ADMIN')")
	// public void addPlayerToLeague(@RequestBody PlayerLeague playerLeague)
	// {
	// log.debug("playerLeague ="+ playerLeague.toString());
	//
	// leagueService.joinLeague(playerLeague);
	//
	// }
	//
	// @RequestMapping(method=RequestMethod.GET, value="/name/{name}",produces =
	// MediaType.APPLICATION_JSON)
	// public @ResponseBody League getLeagueByName(@PathVariable String name)
	// {
	// return leagueService.getLeagueByName(name);
	// }
	//
	// @RequestMapping(method=RequestMethod.DELETE, value="/player")
	// public void removePlayerFromLeagye(@RequestBody PlayerLeague
	// playerLeague)
	// {
	// leagueService.removePlayerFromLeagye(playerLeague.getLeagueId(),
	// playerLeague.getPlayerId());
	// }
	//
	// @RequestMapping(method=RequestMethod.GET,
	// value="/player/leagueid/{leagueid}")
	// public @ResponseBody Set<String> getPlayersInLeague(@PathVariable String
	// leagueid)
}

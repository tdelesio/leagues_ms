package com.makeurpicks.service;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.makeurpicks.SeasonApplication;
import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.Team;
import com.makeurpicks.domain.TeamBuilder;
import com.makeurpicks.repository.TeamRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeasonApplication.class)
@IntegrationTest({ "server.port:0", "spring.cloud.config.enabled:true" })
//eureka.client.enabled=false",
@WebAppConfiguration
public class TeamServiceTest {

	@Autowired
	private TeamRepository teamRepository;
	
	@Autowired
	private TeamService teamService;
	
	@Before
	public void setup()
	{
		teamRepository.deleteAll();
		
		
	}

	
	@Test
	public void testGetTeams() {
		teamService.createTeams(LeagueType.PICKEM.toString());
		
		Map<String, Team> teams = teamService.getTeams(LeagueType.PICKEM.toString());
		
		Assert.assertEquals(32, teams.size());
		
		teamService.createTeams(LeagueType.PICKEM.toString());
		
		Assert.assertEquals(32, teams.size());
	}
}

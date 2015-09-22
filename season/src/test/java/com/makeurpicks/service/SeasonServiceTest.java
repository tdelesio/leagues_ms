package com.makeurpicks.service;

import java.util.Calendar;

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
import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.SeasonBuilder;
import com.makeurpicks.repository.SeasonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SeasonApplication.class)
@IntegrationTest({ "server.port:0", "spring.cloud.config.enabled:true" })
//eureka.client.enabled=false",
@WebAppConfiguration
public class SeasonServiceTest {

	@Autowired
	private SeasonRepository seasonRepository;
	
	@Autowired
	private SeasonService seasonService;
	
	@Before
	public void setup()
	{
		seasonRepository.deleteAll();
		
	}
	
	@Test
	public void testSeason()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		
		Season season = new SeasonBuilder()
			.withStartYear(currentYear)
			.withEndYear(currentYear+1)
			.withLeagueType(LeagueType.PICKEM)
			.build();
		
		season = seasonService.createSeason(season);
		
		Assert.assertTrue(seasonService.getCurrentSeasons().contains(season));
	}
	
}

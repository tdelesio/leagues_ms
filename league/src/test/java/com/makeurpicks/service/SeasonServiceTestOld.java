package com.makeurpicks.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.makeurpicks.LeagueApplication;
import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.SeasonBuilder;
import com.makeurpicks.repository.SeasonRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LeagueApplication.class)
public class SeasonServiceTestOld {

	@Mock
	private SeasonRepository seasonRepositoryMock;
	
	@Autowired
	@InjectMocks
	private SeasonService seasonService;
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testSeason()
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		
		Season season = new SeasonBuilder(UUID.randomUUID().toString())
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		
		when(seasonRepositoryMock.save(season)).thenReturn(season);
		when(seasonRepositoryMock.getSeasonsByLeagueType(LeagueType.pickem.toString())).thenReturn(Arrays.asList(season));
		
		
		
		season = seasonService.createSeason(season);
		
		Assert.assertTrue(seasonService.getCurrentSeasons().contains(season));
		
	}
	
}

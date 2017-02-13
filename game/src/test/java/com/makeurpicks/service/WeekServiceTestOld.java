package com.makeurpicks.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jboss.util.id.GUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.GameApplication;
import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.domain.Week;
import com.makeurpicks.domain.WeekBuilder;
import com.makeurpicks.repository.WeekRepository;


@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = GameApplication.class)
public class WeekServiceTestOld {

	@Mock
	private WeekRepository weekRepository;
	
	@Mock
	private LeagueIntegrationService leagueIntServiceMock;
	
	@Autowired
	@InjectMocks
	private WeekService weekService;
	
	
	@Test
	public void testGetWeeksBySeason() {
		String s2015 = UUID.randomUUID().toString();
		String s2016 = UUID.randomUUID().toString();
		
		
		Week week1 = createWeek(s2015, 1);
		Week week2 = createWeek(s2015, 2);
		Week week3 = createWeek(s2015, 3);
		
		Week weeka = createWeek(s2016, 1);
		Week weekb = createWeek(s2016, 2);
		
		when(weekRepository.save(week1)).thenReturn(week1);
		when(weekRepository.save(week2)).thenReturn(week2);
		when(weekRepository.save(week3)).thenReturn(week3);
		when(weekRepository.save(weeka)).thenReturn(weeka);
		when(weekRepository.save(weekb)).thenReturn(weekb);
		
		List<Week> weeks2015 = new ArrayList<>();
		weeks2015.add(week1);
		weeks2015.add(week2);
		weeks2015.add(week3);
		
		List<Week> weeks2016 = new ArrayList<>();
		weeks2016.add(weeka);
		weeks2016.add(weekb);
		
		when(weekRepository.findBySeasonId(s2015)).thenReturn(weeks2015);
		when(weekRepository.findBySeasonId(s2016)).thenReturn(weeks2016);
		
		List<Week> weeks = weekService.getWeeksBySeason(s2015);
		assertTrue(weeks.contains(week1));
		assertTrue(weeks.contains(week2));
		assertTrue(weeks.contains(week3));
		
		assertFalse(weeks.contains(weeka));
		assertFalse(weeks.contains(weekb));
		
		weeks = weekService.getWeeksBySeason(s2016);
		assertFalse(weeks.contains(week1));
		assertFalse(weeks.contains(week2));
		assertFalse(weeks.contains(week3));
		
		assertTrue(weeks.contains(weeka));
		assertTrue(weeks.contains(weekb));
	}

	@Test
	public void getWeeksByLeague()
	{
		String leagueId = UUID.randomUUID().toString();
		String s2015 = UUID.randomUUID().toString();
		String s2016 = UUID.randomUUID().toString();
		
		LeagueView leagueView = new LeagueView();
		leagueView.setId(leagueId);
		leagueView.setSeasonId(s2016);
		leagueView.setLeagueName("Pickem 2016");
		
		Week week1 = createWeek(s2015, 1);
		Week week2 = createWeek(s2015, 2);
		Week week3 = createWeek(s2015, 3);
		
		Week weeka = createWeek(s2016, 1);
		Week weekb = createWeek(s2016, 2);
		
		
		when(weekRepository.save(week2)).thenReturn(week2);
		when(weekRepository.save(week3)).thenReturn(week3);
		when(weekRepository.save(weeka)).thenReturn(weeka);
		when(weekRepository.save(weekb)).thenReturn(weekb);
		
		when(leagueIntServiceMock.getLeagueById(leagueId)).thenReturn(leagueView);
		
		List<Week> weeks2015 = new ArrayList<>();
		weeks2015.add(week1);
		weeks2015.add(week2);
		weeks2015.add(week3);
		
		List<Week> weeks2016 = new ArrayList<>();
		weeks2016.add(weeka);
		weeks2016.add(weekb);
		when(weekRepository.findBySeasonId(s2015)).thenReturn(weeks2015);
		when(weekRepository.findBySeasonId(s2016)).thenReturn(weeks2016);
		
		List<Week> weeks = weekService.getWeeksByLeague(leagueId);
		
		assertFalse(weeks.contains(week1));
		assertFalse(weeks.contains(week2));
		assertFalse(weeks.contains(week3));
		
		assertTrue(weeks.contains(weeka));
		assertTrue(weeks.contains(weekb));
	}

	private Week createWeek(String seasonId, int weekNumber)
	{
		Week week = new WeekBuilder()
			.withSeasonId(seasonId)
			.withWeekNumber(weekNumber)
			.build();
		
		weekService.createWeek(week);
		
		return week;	
	}
}

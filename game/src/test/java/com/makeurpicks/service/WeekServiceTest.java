package com.makeurpicks.service;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.GameApplication;
import com.makeurpicks.domain.Week;
import com.makeurpicks.domain.WeekBuilder;
import com.makeurpicks.repository.WeekRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = GameApplication.class)
public class WeekServiceTest {

	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private WeekService weekService;
	
	@Before
	public void setup()
	{
		weekRepository.deleteAll();
	}
	
	@Test
	public void testGetWeeksBySeason() {
		Week week1 = createWeek("1", 1);
		Week week2 = createWeek("1", 2);
		Week week3 = createWeek("1", 3);
		
		Week weeka = createWeek("a", 1);
		Week weekb = createWeek("a", 2);
		
		List<Week> weeks = weekService.getWeeksBySeason("1");
		assertTrue(weeks.contains(week1));
		assertTrue(weeks.contains(week2));
		assertTrue(weeks.contains(week3));
		
		assertFalse(weeks.contains(weeka));
		assertFalse(weeks.contains(weekb));
	}

	@Test
	public void testCreateWeek() {

		Week week = createWeek("1", 1);
		
		assertThat(week.getId(), instanceOf(String.class));
		
		assertEquals(week, weekRepository.findOne(week.getId()));
	}

	private Week createWeek(String seasonId, int weekNumber)
	{
		Week week = new WeekBuilder()
			.withSeasonId(seasonId)
			.withWeekNumber(weekNumber)
			.build();
		
		week = weekService.createWeek(week);
		
		return week;	
	}
}

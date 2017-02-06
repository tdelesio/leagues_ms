package com.makeurpicks.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;

import com.makeurpicks.GameApplication;
import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.domain.Week;
import com.makeurpicks.domain.WeekBuilder;
import com.makeurpicks.repository.WeekRepository;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = GameApplication.class)
public class WeekServiceTest {

	@Mock
	private WeekRepository weekRepositoryMock;

	@Mock
	private LeagueIntegrationService leagueIntServiceMock;

	@Autowired
	@InjectMocks
	private WeekService weekService;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	/*@Test
	public void getWeeksBySeason_seasonIdIsNull_throwsNullPointerException() {
		expectedEx.expect(NullPointerException.class);
		String seasonId = UUID.randomUUID().toString();
		when(weekRepositoryMock.findBySeasonId(seasonId)).thenReturn(null);
		weekService.getWeeksBySeason(seasonId);
	}*/

	@Test
	public void getWeeksBySeason_seasonIdIsNotNull_returnsListOfWeeks() {
		String seasonId = UUID.randomUUID().toString();
		Week week1 = new Week();
		week1.setId(UUID.randomUUID().toString());
		week1.setSeasonId(seasonId);
		week1.setWeekNumber((int) (Math.random() + 1));

		Week week2 = new Week();
		week2.setId(UUID.randomUUID().toString());
		week2.setSeasonId(seasonId);
		week2.setWeekNumber((int) (Math.random() + 1));
		
		Week week3 = new Week();
		week3.setId(UUID.randomUUID().toString());
		week3.setSeasonId(UUID.randomUUID().toString());
		week3.setWeekNumber((int) (Math.random() + 1));
		
		when(weekRepositoryMock.save(week1)).thenReturn(week1);
		when(weekRepositoryMock.save(week2)).thenReturn(week2);
		when(weekRepositoryMock.save(week3)).thenReturn(week3);

		List<Week> weeksPair1 = new ArrayList<>();
		weeksPair1.add(week1);
		weeksPair1.add(week2);
//		List<Week> weeksPair2 = new ArrayList<>();
//		weeksPair2.add(week3);

		when(weekRepositoryMock.findBySeasonId(seasonId)).thenReturn(weeksPair1);
		

		weekService.getWeeksBySeason(seasonId);
	}
	
	
	/*@Test
	public void getWeeksByLeague_leagueIdIsNull_throwsNullPointerException() {
		expectedEx.expect(NullPointerException.class);
		String leagueId = UUID.randomUUID().toString();
		LeagueView leagueView = new LeagueView();
		when(leagueIntServiceMock.getLeagueById(leagueId)).thenReturn(null);
		when(weekService.getWeeksBySeason(leagueView.getSeasonId())).thenReturn(null);
		weekService.getWeeksByLeague(leagueId);
	}*/
	
	@Test
	public void getWeeksByLeague_leagueIdIsNotNull_returnsListOfWeeks() {
		String leagueId = UUID.randomUUID().toString();
		String seasonId = UUID.randomUUID().toString();
		Week week1 = new Week();
		week1.setId(UUID.randomUUID().toString());
		week1.setSeasonId(seasonId);
		week1.setWeekNumber((int) (Math.random() + 1));

		Week week2 = new Week();
		week2.setId(UUID.randomUUID().toString());
		week2.setSeasonId(seasonId);
		week2.setWeekNumber((int) (Math.random() + 1));
		
		Week week3 = new Week();
		week3.setId(UUID.randomUUID().toString());
		week3.setSeasonId(UUID.randomUUID().toString());
		week3.setWeekNumber((int) (Math.random() + 1));
		
		when(weekRepositoryMock.save(week1)).thenReturn(week1);
		when(weekRepositoryMock.save(week2)).thenReturn(week2);
		when(weekRepositoryMock.save(week3)).thenReturn(week3);

		List<Week> weeksPair1 = new ArrayList<>();
		weeksPair1.add(week1);
		weeksPair1.add(week2);
		List<Week> weeksPair2 = new ArrayList<>();
		weeksPair2.add(week3);
		LeagueView leagueView =new LeagueView();
		leagueView.setId(UUID.randomUUID().toString());
		leagueView.setLeagueId(UUID.randomUUID().toString());
		leagueView.setSeasonId(seasonId);
		leagueView.setLeagueName("newLeague");
		when(leagueIntServiceMock.getLeagueById(leagueId)).thenReturn(leagueView);
		when(weekService.getWeeksBySeason(leagueView.getSeasonId())).thenReturn(weeksPair1); 
		weekService.getWeeksByLeague(leagueId);
	}


	@Test
	public void createWeekShouldCallSaveOnWeekRepository() {
		String seasonId = UUID.randomUUID().toString();
		int weekNumber = (int) (Math.random() + 1);
		Week week = new WeekBuilder().withSeasonId(seasonId).withWeekNumber(weekNumber).build();

		weekService.createWeek(week);

		verify(weekRepositoryMock).save(week);
	}
}

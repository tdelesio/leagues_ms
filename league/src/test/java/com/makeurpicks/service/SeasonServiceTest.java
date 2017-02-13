package com.makeurpicks.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.Season;
import com.makeurpicks.domain.SeasonBuilder;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.repository.SeasonRepository;

public class SeasonServiceTest {

	@Mock
	private SeasonRepository seasonRepositoryMock;
	
	@Autowired
	@InjectMocks
	private SeasonService seasonService;
	
	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@Before
	public void setup()
	{
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getCurrentSeasonsTest()
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
	
	
	@Test
	public void createSeasonShouldCallSaveOnSeasonRepository() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		
		Season season = new SeasonBuilder(UUID.randomUUID().toString())
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		seasonService.createSeason(season);
		verify(seasonRepositoryMock).save(season);
	}
	
	@Test
	public void updateSeasonShouldCallUpdateOnSeasonRepository() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		
		Season season = new SeasonBuilder(UUID.randomUUID().toString())
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		when(seasonRepositoryMock.findOne(season.getId())).thenReturn(season);
		seasonService.updateSeason(season);
		verify(seasonRepositoryMock).save(season);
	}
	
	@Test
	public void updateSeasonOnNonExistingSeasonThrowsLeagueValidationExceptionLeagueNotFound() {
		expectedEx.expect(LeagueValidationException.class);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		String id=UUID.randomUUID().toString();
		Season season = new SeasonBuilder(id)
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		when(seasonRepositoryMock.findOne(id)).thenReturn(null);
		seasonService.updateSeason(season);
	}
	
	@Test
	public void deleteSeasonShouldCallDeleteOnSeasonRepository() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		String seasonId=UUID.randomUUID().toString();
		Season season = new SeasonBuilder(seasonId)
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		when(seasonRepositoryMock.findOne(season.getId())).thenReturn(season);
		seasonService.deleteSeason(seasonId);
		verify(seasonRepositoryMock).delete(seasonId);
	}
	
	@Test
	public void deleteSeasonOnNonExistingSeasonThrowsLeagueValidationExceptionLeagueNotFound() {
		expectedEx.expect(LeagueValidationException.class);
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		String seasonId=UUID.randomUUID().toString();
		Season season = new SeasonBuilder(seasonId)
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		when(seasonRepositoryMock.findOne(season.getId())).thenReturn(null);
		seasonService.deleteSeason(seasonId);
	}
	
	
	@Test
	public void getSeasonById_seasonId_returnSeason() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		String seasonId=UUID.randomUUID().toString();
		Season season = new SeasonBuilder(seasonId)
				.withStartYear(currentYear)
				.withEndYear(currentYear+1)
				.withLeagueType(LeagueType.pickem)
				.build();
		when(seasonRepositoryMock.findOne(season.getId())).thenReturn(season);
		assertEquals(season.getId(), seasonId);
	}

}

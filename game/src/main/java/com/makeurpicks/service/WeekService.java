package com.makeurpicks.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.LeagueView;
import com.makeurpicks.domain.Week;
import com.makeurpicks.repository.WeekRepository;

@Component
public class WeekService {

	private Log log = LogFactory.getLog(WeekService.class);
	
	@Autowired
	private WeekRepository weekRepository;
	
	@Autowired
	private LeagueIntegrationService leagueIntegrationService;
	
	public List<Week> getWeeksBySeason(String seasonId)
	{
		List<Week> weeks = weekRepository.findBySeasonId(seasonId);
		Collections.sort(weeks, (w1, w2) -> Integer.compare(w2.getWeekNumber(), w1.getWeekNumber()));
		
		return weeks;
	}

	public List<Week> getWeeksByLeague(String leaugeId)
	{
		log.debug("leagueId="+leaugeId);
		LeagueView leagueView = leagueIntegrationService.getLeagueById(leaugeId);
		return getWeeksBySeason(leagueView.getSeasonId());
	}
	
	public Week createWeek(Week week)
	{
		
		String id = UUID.randomUUID().toString();
		
		week.setId(id);
		return weekRepository.save(week);
	}
	
}

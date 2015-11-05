package com.makeurpicks.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.Week;
import com.makeurpicks.repository.WeekRepository;

@Component
public class WeekService {

	@Autowired
	private WeekRepository weekRepository;
	
	public List<Week> getWeeksBySeason(String seasonId)
	{
		return weekRepository.findBySeasonId(seasonId);
	}
	
	public Week createWeek(Week week)
	{
		
		String id = UUID.randomUUID().toString();
		
		week.setId(id);
		return weekRepository.save(week);
	}
	
}

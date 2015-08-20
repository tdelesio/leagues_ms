package com.makeurpicks.repository;

import com.makeurpicks.domain.Week;

public interface WeekRepository {

	public Week createUpdateWeek(Week week);
	public Iterable<Week> getWeeksBySeason(String seasonId);
}

package com.makeurpicks.domain;

import java.io.Serializable;

public class SetupLeagueJSON implements Serializable {

	private Iterable<LeagueView> leagues;
	private Iterable<WeekView> weeks;
	public Iterable<LeagueView> getLeagues() {
		return leagues;
	}
	public void setLeagues(Iterable<LeagueView> leagues) {
		this.leagues = leagues;
	}
	public Iterable<WeekView> getWeeks() {
		return weeks;
	}
	public void setWeeks(Iterable<WeekView> weeks) {
		this.weeks = weeks;
	}
	
	
	
}

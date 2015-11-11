package com.makeurpicks.domain;

import java.util.List;

import com.makeurpicks.service.league.LeagueView;
import com.makeurpicks.service.week.WeekView;

public class NavigationView {

	private String username;
	private List<LeagueView> leagues;
	private List<WeekView> weeks;
	private String selectedLeagueId;
	private String selectedWeekId;
	
	public String getSelectedLeagueId() {
		return selectedLeagueId;
	}

	public void setSelectedLeagueId(String selectedLeagueId) {
		this.selectedLeagueId = selectedLeagueId;
	}

	public String getSelectedWeekId() {
		return selectedWeekId;
	}

	public void setSelectedWeekId(String selectedWeekId) {
		this.selectedWeekId = selectedWeekId;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public List<LeagueView> getLeagues() {
		return leagues;
	}
	public void setLeagues(List<LeagueView> leagues) {
		this.leagues = leagues;
	}
	public List<WeekView> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<WeekView> weeks) {
		this.weeks = weeks;
	}
	
	
}

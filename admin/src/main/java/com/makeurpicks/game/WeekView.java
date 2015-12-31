package com.makeurpicks.game;

public class WeekView {

	private int weekNumber;
	private String seasonId;
	private String id;
	
	
	public int getWeekNumber() {
		return weekNumber;
	}
	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}
	@Override
	public String toString() {
		return "WeekView [weekNumber=" + weekNumber + ", seasonId=" + seasonId + ", id=" + id + "]";
	}
	
	
}

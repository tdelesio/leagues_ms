package com.makeurpicks.domain;

import javax.persistence.Entity;

@Entity
public class Week extends AbstractModel {

	private int weekNumber;
	private String seasonId;
	public int getWeekNumber() {
		return weekNumber;
	}
	public void setWeekNumber(int weekNumber) {
		this.weekNumber = weekNumber;
	}
	public String getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	
}

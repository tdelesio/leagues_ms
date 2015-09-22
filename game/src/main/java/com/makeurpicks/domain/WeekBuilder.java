package com.makeurpicks.domain;

import java.util.UUID;

public class WeekBuilder {

	private String id;
	private int weekNumber;
	private String seasonId;
	
	public WeekBuilder()
	{
		UUID uuid = UUID.randomUUID();
		this.id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
	}
	
	public Week build()
	{
		Week week = new Week();
		week.setId(id);
		week.setWeekNumber(weekNumber);
		week.setSeasonId(seasonId);
		return week;
	}
	
	public WeekBuilder withWeekNumber(int w)
	{
		this.weekNumber = w;
		return this;
	}
	
	public WeekBuilder withSeasonId(String s)
	{
		this.seasonId = s;
		return this;
	}
}

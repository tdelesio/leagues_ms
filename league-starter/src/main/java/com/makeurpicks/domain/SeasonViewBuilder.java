package com.makeurpicks.domain;

import java.util.UUID;

public class SeasonViewBuilder {

	private String id;
	private int startYear;
	private int endYear;
	private String leagueType;
	
	public SeasonViewBuilder()
	{
		UUID uuid = UUID.randomUUID();
		this.id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
	}
	
	public SeasonViewBuilder(String id)
	{
		this.id = id;
	}
	
	public SeasonView build()
	{
		SeasonView season = new SeasonView();
		season.setId(id);
		season.setEndYear(endYear);
		season.setStartYear(startYear);
		season.setLeagueType(leagueType);
		return season;
	}
	
	public SeasonViewBuilder withStartYear(int s)
	{
		this.startYear = s;
		return this;
	}
	
	public SeasonViewBuilder withEndYear(int e)
	{
		this.endYear = e;
		return this;
	}
	
	public SeasonViewBuilder withLeagueType(String leagueType)
	{
		this.leagueType = leagueType;
		return this;
	}
	
}

package com.makeurpicks.domain;

import java.util.UUID;


public class LeagueViewBuilder {

	private String id;
	private String leagueName;
	private String adminId;
	private boolean spreads;
	private String password;
	private String seasonId;
	
	public LeagueViewBuilder()
	{
		UUID uuid = UUID.randomUUID();
		this.id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
	}
	
	public LeagueViewBuilder(String id)
	{
		this.id = id;
	}
	
	public LeagueView build()
	{
		LeagueView league = new LeagueView();
		league.setId(id);
		league.setLeagueName(leagueName);
		league.setAdminId(adminId);
		league.setSpreads(spreads);
		league.setPassword(password);
		league.setSeasonId(seasonId);
		return league;
	}
	
	public LeagueViewBuilder withName(String name)
	{
		this.leagueName = name;
		return this;
	}
	
	public LeagueViewBuilder withPassword(String password)
	{
		this.password = password;
		return this;
	}
	
	public LeagueViewBuilder withNoSpreads()
	{
		this.spreads=false;
		return this;
	}
	
	public LeagueViewBuilder withAdminId(String adminId)
	{
		this.adminId = adminId;
		return this;
	}
	
	public LeagueViewBuilder withSeasonId(String id)
	{
		this.seasonId = id;
		return this;
	}
}

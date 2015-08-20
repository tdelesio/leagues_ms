package com.makeurpicks.domain;

public class DoublePick extends AbstractModel {

	private String pickId;

	public DoublePick(String leagueId, String weekId, String playerId)
	{
		this.id = buildString(leagueId, weekId, playerId);
	}
	
	public String getPickId() {
		return pickId;
	}

	public void setPickId(String pickId) {
		this.pickId = pickId;
	}
	
	public static String buildString(String leagueId, String weekId, String playerId)
	{
		return new StringBuilder(leagueId).append("+").append(weekId).append("+").append(playerId).toString();
	}
}

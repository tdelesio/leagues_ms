package com.makeurpicks.domain;

public class DoublePick extends AbstractModel {

	private String pickId;

	public DoublePick(String weekId, String playerId)
	{
		this.id = buildString(weekId, playerId);
	}
	
	public String getPickId() {
		return pickId;
	}

	public void setPickId(String pickId) {
		this.pickId = pickId;
	}
	
	public static String buildString(String weekId, String playerId)
	{
		return new StringBuilder(weekId).append("+").append(playerId).toString();
	}
}

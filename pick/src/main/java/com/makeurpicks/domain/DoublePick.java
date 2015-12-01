package com.makeurpicks.domain;

public class DoublePick extends AbstractModel {

	private String pickId;
	private String gameId;
	private boolean hasDoubleGameStarted=false;
	
	public DoublePick(String weekId, String playerId, String pickId, String gameId, boolean gameStarted)
	{
		this.id = buildString(weekId, playerId);
		this.gameId = gameId;
		this.pickId = pickId;
		this.hasDoubleGameStarted = gameStarted;
	}
	
	public String getPickId() {
		return pickId;
	}

	public void setPickId(String pickId) {
		this.pickId = pickId;
	}
	
	
	
//	public String getGameId() {
//		return gameId;
//	}

//	public void setGameId(String gameId) {
//		this.gameId = gameId;
//	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public boolean isHasDoubleGameStarted() {
		return hasDoubleGameStarted;
	}

	public void setHasDoubleGameStarted(boolean hasDoubleGameStarted) {
		this.hasDoubleGameStarted = hasDoubleGameStarted;
	}

	public static String buildString(String weekId, String playerId)
	{
		return new StringBuilder(weekId).append("+").append(playerId).toString();
	}
}

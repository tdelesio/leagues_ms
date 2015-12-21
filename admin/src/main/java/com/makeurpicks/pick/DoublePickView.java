package com.makeurpicks.pick;

public class DoublePickView  {

	private String id;
	private String pickId;
	private String gameId;
	private String leagueId;
	private boolean hasDoubleGameStarted=false;
	private String previousDoubleGameId;
	
	public DoublePickView()
	{
		
	}
	
	public DoublePickView(String leagueId, String weekId, String playerId, String pickId, String gameId, boolean gameStarted)
	{
		this.id = buildString(leagueId, weekId, playerId);
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

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public String getPreviousDoubleGameId() {
		return previousDoubleGameId;
	}

	public void setPreviousDoubleGameId(String previousDoubleGameId) {
		this.previousDoubleGameId = previousDoubleGameId;
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

	
	
	public String getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(String leagueId) {
		this.leagueId = leagueId;
	}

	public static String buildString(String leagueId, String weekId, String playerId)
	{
		return new StringBuilder(leagueId).append("+").append(weekId).append("+").append(playerId).toString();
	}
}

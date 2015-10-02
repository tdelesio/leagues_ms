package com.makeurpicks.domain;

public class Pick extends AbstractModel {

	private String teamId;
	private String leagueId;
	private String playerId;
	private String weekId;
	private String gameId;
	private boolean noPick=false;
	private long pickLastUpdated = System.currentTimeMillis();
	

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(String leagueId) {
		this.leagueId = leagueId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getWeekId() {
		return weekId;
	}

	public void setWeekId(String weekId) {
		this.weekId = weekId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public boolean isNoPick() {
		return noPick;
	}

	public void setNoPick(boolean noPick) {
		this.noPick = noPick;
	}

	public long getPickLastUpdated() {
		return pickLastUpdated;
	}


	
}

package com.makeurpicks.pick;

public class PickView   {

	private String id;
	
	private String teamId;
	private String leagueId;
	private String playerId;
	private String weekId;
	private String gameId;
	private boolean noPick=false;
	private long pickLastUpdated = System.currentTimeMillis();
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	@Override
	public String toString() {
		return "Pick [teamId=" + teamId + ", playerId=" + playerId + ", weekId=" + weekId + ", gameId=" + gameId
				+ ", noPick=" + noPick + ", pickLastUpdated=" + pickLastUpdated + "]";
	}


	
}

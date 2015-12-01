package com.makeurpicks.service.pick;

public class DoublePickView {

	private String id="";
	private String pickId="";
	private String gameId="";
	private boolean hasDoubleGameStarted=false;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPickId() {
		return pickId;
	}
	public void setPickId(String pickId) {
		this.pickId = pickId;
	}
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
	
	
}

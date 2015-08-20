package com.makeurpicks.domain;

public class GameResponse extends AbstractModel {

	private String favoriteTeamId;
	private String dogTeamId;
	private String weekId;
	private long gameStartTime;
	
	public GameResponse()
	{
		
	}
	
	public GameResponse(String id)
	{
		super.id = id;
	}
	
	public GameResponse(String id, long gameStartTime, String favId, String dogId, String weekId)
	{
		super.id = id;
		this.gameStartTime = gameStartTime;
		this.favoriteTeamId = favId;
		this.dogTeamId = dogId;
		this.weekId = weekId;
	}
	
	public String getFavoriteTeamId() {
		return favoriteTeamId;
	}
	public void setFavoriteTeamId(String favoriteTeamId) {
		this.favoriteTeamId = favoriteTeamId;
	}
	public String getDogTeamId() {
		return dogTeamId;
	}
	public void setDogTeamId(String dogTeamId) {
		this.dogTeamId = dogTeamId;
	}
	public long getGameStartTime() {
		return gameStartTime;
	}
	public void setGameStartTime(long gameStartTime) {
		this.gameStartTime = gameStartTime;
	}
	
	
	
	public String getWeekId() {
		return weekId;
	}
	public void setWeekId(String weekId) {
		this.weekId = weekId;
	}
	public boolean hasGameStarted()
	{
		if (System.currentTimeMillis() > gameStartTime)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
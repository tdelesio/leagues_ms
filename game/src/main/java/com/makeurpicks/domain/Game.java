package com.makeurpicks.domain;

public class Game extends AbstractModel{

	private double spread = 0.5;
	private int favScore=0;
	private int dogScore=0;
	private boolean favHome=true;
	private String favId;
	private String dogId;
	private long gameStart;
	private String weekId;
	public double getSpread() {
		return spread;
	}
	public void setSpread(double spread) {
		this.spread = spread;
	}
	public int getFavScore() {
		return favScore;
	}
	public void setFavScore(int favScore) {
		this.favScore = favScore;
	}
	public int getDogScore() {
		return dogScore;
	}
	public void setDogScore(int dogScore) {
		this.dogScore = dogScore;
	}
	public boolean isFavHome() {
		return favHome;
	}
	public void setFavHome(boolean favHome) {
		this.favHome = favHome;
	}
	public String getFavId() {
		return favId;
	}
	public void setFavId(String favId) {
		this.favId = favId;
	}
	public String getDogId() {
		return dogId;
	}
	public void setDogId(String dogId) {
		this.dogId = dogId;
	}
	public long getGameStart() {
		return gameStart;
	}
	public void setGameStart(long gameStart) {
		this.gameStart = gameStart;
	}
	public String getWeekId() {
		return weekId;
	}
	public void setWeekId(String weekId) {
		this.weekId = weekId;
	}
	
	
}

package com.makeurpicks.domain;

public class SeasonStats extends AbstractModel {

	private String username;
	private int wins;
	private int loses;
	private double spreadPoints;
	

	public String getUsername() {
		return username;
	}
	
	public void addWin(int weight)
	{
		wins+=weight;
	}
	
	public void addLoses(int weight)
	{
		loses+=weight;
	}
	
	public void addSpread(double spread)
	{
		spreadPoints+=spread;
	}
	public void subtractSpread(double spread)
	{
		spreadPoints-=spread;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLoses() {
		return loses;
	}
	public void setLoses(int loses) {
		this.loses = loses;
	}
	public double getSpreadPoints() {
		return spreadPoints;
	}
	public void setSpreadPoints(double spreadPoints) {
		this.spreadPoints = spreadPoints;
	}

	
	
	
}

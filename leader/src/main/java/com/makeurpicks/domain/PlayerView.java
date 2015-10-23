package com.makeurpicks.domain;

public class PlayerView extends AbstractModel {

	private String username;
	private int wins;
	
	public String getUsername() {
		return username;
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
	
	
}

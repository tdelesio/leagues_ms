package com.makeurpicks.league;

import java.io.Serializable;

public class PlayerView implements Serializable  {

	public PlayerView()
	{
		
	}
	
	public PlayerView(String i)
	{
		this.id = i;
	}
	
	public String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
}

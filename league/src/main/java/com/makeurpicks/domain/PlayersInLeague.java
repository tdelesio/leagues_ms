package com.makeurpicks.domain;

import java.util.HashSet;
import java.util.Set;

public class PlayersInLeague extends AbstractModel {

	private Set<String> players;
	
	public void addPlayer(String player)
	{
		if (players == null)
			players = new HashSet<String>();
		
		players.add(player);
	}
	
	public void removePlayer(String player)
	{
		if (players == null)
			return;
		players.remove(player);
		
	}

	public Set<String> getPlayers() {
		return players;
	}

	public void setPlayers(Set<String> players) {
		this.players = players;
	}
	
	
}

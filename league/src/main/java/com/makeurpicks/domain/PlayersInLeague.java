package com.makeurpicks.domain;

import java.util.HashSet;
import java.util.Set;

public class PlayersInLeague extends AbstractModel {

	private Set<PlayerResponse> players;
	
	public void addPlayer(PlayerResponse player)
	{
		if (players == null)
			players = new HashSet<PlayerResponse>();
		
		players.add(player);
	}
	
	public void removePlayer(PlayerResponse player)
	{
		if (players == null)
			return;
		players.remove(player);
		
	}

	public Set<PlayerResponse> getPlayers() {
		return players;
	}

	public void setPlayers(Set<PlayerResponse> players) {
		this.players = players;
	}
	
	
}

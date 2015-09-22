package com.makeurpicks.domain;

import java.util.HashSet;
import java.util.Set;


public class LeaguesPlayerJoined extends AbstractModel {

	private Set<LeagueName> leagues;

	public Set<LeagueName> getLeauges() {
		return leagues;
	}

	public void setLeauges(Set<LeagueName> leauges) {
		this.leagues = leauges;
	}
	
	public void addLeagueName(LeagueName leagueName)
	{ 
		if (leagues == null)
			leagues = new HashSet<LeagueName>();
		
		leagues.add(leagueName);
	}
	
	public void removeLeagueName(LeagueName leagueName)
	{ 
		if (leagues == null)
			return;
		
		leagues.remove(leagueName);
	}
	
	
	
}

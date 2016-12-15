package com.makeurpicks.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class PlayerLeague  {
	@Id
	private String id;
	@ManyToMany(mappedBy = "playersLeague")
	private  Collection<League> league;
	private String leagueName;
	private String playerId;
	private String password;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Collection<League> getLeague() {
		return league;
	}
	public void setLeague(Collection<League> league) {
		this.league = league;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLeagueName() {
		return leagueName;
	}
	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}
	
	
}

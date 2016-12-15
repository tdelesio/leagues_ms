package com.makeurpicks.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import scala.collection.mutable.Publisher;

@Entity
public class League {
	@Id
	private String id;
	private String leagueName;
	private int paidFor = 0;
	private boolean money = false;
	private boolean free;
	private boolean active;
	private String password;
	private boolean spreads = true;

	private boolean doubleEnabled = true;
	private double entryFee;
	private double weeklyFee;
	private int firstPlacePercent;
	private int secondPlacePercent;
	private int thirdPlacePercent;
	private int fourthPlacePercent;
	private int fifthPlacePercent;
	private int doubleType;
	private boolean banker;

	private String seasonId;
	private String adminId;
	private Collection<PlayerLeague> playersLeague = new ArrayList<PlayerLeague>();

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "leaguejoinplayer", joinColumns = @JoinColumn(name = "league_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "player_id", referencedColumnName = "id"))
	public Collection<PlayerLeague> getPlayersLeague() {
		return playersLeague;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public int getPaidFor() {
		return paidFor;
	}

	public void setPaidFor(int paidFor) {
		this.paidFor = paidFor;
	}

	public boolean isMoney() {
		return money;
	}

	public void setMoney(boolean money) {
		this.money = money;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isSpreads() {
		return spreads;
	}

	public void setSpreads(boolean spreads) {
		this.spreads = spreads;
	}

	public boolean isDoubleEnabled() {
		return doubleEnabled;
	}

	public void setDoubleEnabled(boolean doubleEnabled) {
		this.doubleEnabled = doubleEnabled;
	}

	public double getEntryFee() {
		return entryFee;
	}

	public void setEntryFee(double entryFee) {
		this.entryFee = entryFee;
	}

	public double getWeeklyFee() {
		return weeklyFee;
	}

	public void setWeeklyFee(double weeklyFee) {
		this.weeklyFee = weeklyFee;
	}

	public int getFirstPlacePercent() {
		return firstPlacePercent;
	}

	public void setFirstPlacePercent(int firstPlacePercent) {
		this.firstPlacePercent = firstPlacePercent;
	}

	public int getSecondPlacePercent() {
		return secondPlacePercent;
	}

	public void setSecondPlacePercent(int secondPlacePercent) {
		this.secondPlacePercent = secondPlacePercent;
	}

	public int getThirdPlacePercent() {
		return thirdPlacePercent;
	}

	public void setThirdPlacePercent(int thirdPlacePercent) {
		this.thirdPlacePercent = thirdPlacePercent;
	}

	public int getFourthPlacePercent() {
		return fourthPlacePercent;
	}

	public void setFourthPlacePercent(int fourthPlacePercent) {
		this.fourthPlacePercent = fourthPlacePercent;
	}

	public int getFifthPlacePercent() {
		return fifthPlacePercent;
	}

	public void setFifthPlacePercent(int fifthPlacePercent) {
		this.fifthPlacePercent = fifthPlacePercent;
	}

	public int getDoubleType() {
		return doubleType;
	}

	public void setDoubleType(int doubleType) {
		this.doubleType = doubleType;
	}

	public boolean isBanker() {
		return banker;
	}

	public void setBanker(boolean banker) {
		this.banker = banker;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public String getAdminId() {
		return adminId;
	}

	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

}

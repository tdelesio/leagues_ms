package com.makeurpicks.game;

import java.time.ZonedDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.makeurpicks.domain.AbstractModel;

public class GameResponse extends AbstractModel {

	private double spread = 0.5;
	private String seasonId;
	private String favId;
	private String dogId;
	private String weekId;
	
	private int favScore=0;
	private int dogScore=0;
	private boolean favHome=true;
	
	@DateTimeFormat(iso = ISO.DATE_TIME)
//	@DateTimeFormat(pattern="YYYY-MM-DDTHH:mm:ss.sssZ")
	private ZonedDateTime gameStart;
	
	//aggegrated data
	private String favFullName;
	private String dogFullName;
	private String dogShortName;
	private String favShortName;
	
	public GameResponse()
	{
		
	}
	
	public GameResponse(String id, ZonedDateTime gameStartTime, String favId, String dogId, String weekId)
	{
		super.id = id;
		this.gameStart = gameStartTime;
		this.favId = favId;
		this.dogId = dogId;
		this.weekId = weekId;
	}
	
	public double getSpread() {
		return spread;
	}
	public void setSpread(double spread) {
		this.spread = spread;
	}
	public String getSeasonId() {
		return seasonId;
	}
	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
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
	public String getWeekId() {
		return weekId;
	}
	public void setWeekId(String weekId) {
		this.weekId = weekId;
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
	public ZonedDateTime getGameStart() {
		return gameStart;
	}
	public void setGameStart(ZonedDateTime gameStart) {
		this.gameStart = gameStart;
	}
	public String getFavFullName() {
		return favFullName;
	}
	public void setFavFullName(String favFullName) {
		this.favFullName = favFullName;
	}
	public String getDogFullName() {
		return dogFullName;
	}
	public void setDogFullName(String dogFullName) {
		this.dogFullName = dogFullName;
	}
	public String getDogShortName() {
		return dogShortName;
	}
	public void setDogShortName(String dogShortName) {
		this.dogShortName = dogShortName;
	}
	public String getFavShortName() {
		return favShortName;
	}
	public void setFavShortName(String favShortName) {
		this.favShortName = favShortName;
	}
	
	public boolean getHasGameStarted()
	{
		return gameStart.isBefore(ZonedDateTime.now());
	}

}
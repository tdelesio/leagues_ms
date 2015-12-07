package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.List;

import com.makeurpicks.game.GameView;
import com.makeurpicks.game.WeekView;
import com.makeurpicks.league.LeagueView;
import com.makeurpicks.pick.DoublePickView;
import com.makeurpicks.pick.PickView;
import com.makeurpicks.season.SeasonView;

public class Dummy {

	private List<GameView> games=new ArrayList<>();
	private List<SeasonView> seasons=new ArrayList<>();
	private List<PickView> picks=new ArrayList<>();
	private List<DoublePickView> doubles=new ArrayList<>();
	private List<WeekView> weeks=new ArrayList<>();
	private List<LeagueView> leagues = new ArrayList<>();
	
	public void addLeague(LeagueView lv)
	{
		leagues.add(lv);
	}
	public void addGame(GameView gv)
	{
		games.add(gv);
	}
	
	public void addSeason(SeasonView sv)
	{
		seasons.add(sv);
	}
	
	public void addPick(PickView pv)
	{
		picks.add(pv);
	}
	
	public void addDouble(DoublePickView dv)
	{
		doubles.add(dv);
	}
	
	public void addWeek(WeekView wv)
	{
		weeks.add(wv);
	}
	
	public List<GameView> getGames() {
		return games;
	}
	public void setGames(List<GameView> games) {
		this.games = games;
	}
	public List<SeasonView> getSeasons() {
		return seasons;
	}
	public void setSeasons(List<SeasonView> seasons) {
		this.seasons = seasons;
	}
	public List<PickView> getPicks() {
		return picks;
	}
	public void setPicks(List<PickView> picks) {
		this.picks = picks;
	}
	public List<DoublePickView> getDoubles() {
		return doubles;
	}
	public void setDoubles(List<DoublePickView> doubles) {
		this.doubles = doubles;
	}
	public List<WeekView> getWeeks() {
		return weeks;
	}
	public void setWeeks(List<WeekView> weeks) {
		this.weeks = weeks;
	}
	public List<LeagueView> getLeagues() {
		return leagues;
	}
	public void setLeagues(List<LeagueView> leagues) {
		this.leagues = leagues;
	}
	
	
	
	
}

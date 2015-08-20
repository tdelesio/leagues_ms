package com.makeurpicks.repository;

import com.makeurpicks.domain.Game;


public interface GameRepository {

	public Game createUpdateGame(Game game);
	public Iterable<Game> getGamesByWeek(String weekId);
	public Game getGameById(String gameId);
}

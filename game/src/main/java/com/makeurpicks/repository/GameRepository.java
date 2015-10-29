package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.makeurpicks.domain.Game;

@Repository
public interface GameRepository extends CrudRepository<Game, String>{

	public List<Game> findByWeekId(String weekId);
	public List<Game> findByWeekIdOrderByGameStart(String weekId);
}

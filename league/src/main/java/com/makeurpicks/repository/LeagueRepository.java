package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.League;

public interface LeagueRepository extends CrudRepository<League, Integer>{
	public List<String> findPlayerIdsByLeagueName(String leagueName);
	public List<String> findPlayerIdsById(int id);
}

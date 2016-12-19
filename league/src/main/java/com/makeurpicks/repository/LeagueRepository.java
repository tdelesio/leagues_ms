package com.makeurpicks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.makeurpicks.domain.League;
@Repository
public interface LeagueRepository extends JpaRepository<League, Integer>{
	public List<String> findPlayerIdsByLeagueName(String leagueName);
	public List<String> findPlayerIdsById(int id);
	public League findByLeagueName(String leagueName);
}

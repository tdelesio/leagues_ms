package com.makeurpicks.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.makeurpicks.domain.Player;

@Repository
public interface PlayerDao extends CrudRepository<Player, String> {

	public Player findByUsername(String username);
}

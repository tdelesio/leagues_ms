package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.Player;

public interface PlayerRepository extends CrudRepository<Player, String>{

}

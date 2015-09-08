package com.makeurpicks.repository;

import org.springframework.data.repository.CrudRepository;

import com.makeurpicks.domain.Player;

public interface PlayerByUsernameRepository extends CrudRepository<Player, String>{

}

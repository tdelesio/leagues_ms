package com.makeurpicks.service;

import org.springframework.stereotype.Component;

import com.makeurpicks.domain.Player;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class PlayerService {

	  public Player defaultPlayer(String id){
	    	return null;
	    }
	    
	    @HystrixCommand(fallbackMethod = "defaultPlayer")
	    public Player getPlayer(String id){
	    	Player player = new Player();
	    	player.setId(id);
	    	
	    	System.out.println(player.getId());
	    	return player;
	    }
}

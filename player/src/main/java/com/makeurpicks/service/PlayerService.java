package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.Player;
import com.makeurpicks.domain.Player.MemberLevel;
import com.makeurpicks.domain.User;
import com.makeurpicks.exception.PlayerValidationException;
import com.makeurpicks.exception.PlayerValidationException.PlayerExceptions;
import com.makeurpicks.repository.PlayerByUsernameRepository;
import com.makeurpicks.repository.PlayerRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class PlayerService {

		@Autowired
		private PlayerRepository playerRepository;
		
		@Autowired PlayerByUsernameRepository playerByUsernameRepository;
		
	 	public Player defaultGetPlayer(String id){
	    	return null;
	    }
	    
	    @HystrixCommand(fallbackMethod = "defaultGetPlayer")
	    public Player getPlayer(String id){
	    	return playerRepository.findOne(id);
	    }
	    
	    public Player login(User user)
		{
			Player player = playerByUsernameRepository.findOne(user.getUsername());
			if (player == null)
				throw new PlayerValidationException(PlayerExceptions.USER_NOT_FOUND);
			
			return player;
		}
		
		public  User updatePassword(User user)
		{
			validatePassword(user.getPassword());
			
			Player player = playerByUsernameRepository.findOne(user.getUsername());
			if (player == null)
			{
				throw new PlayerValidationException(PlayerExceptions.PLAYER_IS_NULL);
			}
			player.setPassword(user.getPassword());
			
			playerByUsernameRepository.save(player);
			playerRepository.save(player);
			
			return user;
		}
		
		public  boolean initiateUpdatePasswordRequest(User user)
		{
			return true;
		}
		
		public  Player getPlayerByUserName(String username)
		{
			return playerByUsernameRepository.findOne(username);
		}
		
		public Player register(Player player)
		{
			validatePlayer(player);
			
			UUID uuid = UUID.randomUUID();
			String id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
			
			player.setId(id);
			player.setMemberLevel(MemberLevel.USER);
			
			playerByUsernameRepository.save(player);
			playerRepository.save(player);
			
			return player;
		} 
		
		private void validatePassword(String password)
		{
			validatePassword(new ArrayList<PlayerExceptions>(), password);
		}
		
		private void validatePassword(List<PlayerExceptions> codes, String password)
		{
			if ("".equals(password))
				codes.add(PlayerExceptions.PASSWORD_IS_NULL);
		}
		
		private void validatePlayer(Player player)
		{
			List<PlayerExceptions> codes = new ArrayList<PlayerExceptions>();
			
			
			if (player == null || "".equals(player.getId()))
			{
				throw new PlayerValidationException(PlayerExceptions.PLAYER_IS_NULL);
			}
			
			if ("".equals(player.getEmail()))
				codes.add(PlayerExceptions.EMAIL_IS_NULL);
			
			validatePassword(codes, player.getPassword());
			
			if ("".equals(player.getUsername()))
				codes.add(PlayerExceptions.USERNAME_IS_NULL);
			else
			{
				Player nameCheck = playerByUsernameRepository.findOne(player.getUsername());
				if (nameCheck != null)
					codes.add(PlayerExceptions.USERNAME_TAKE);
			}
					
			if (!codes.isEmpty())
				throw new PlayerValidationException(codes.toArray(new PlayerExceptions[codes.size()]));
		} 
	    
	    
}

package com.makeurpicks.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.makeurpicks.AuthServerApplication;
import com.makeurpicks.domain.Player;
import com.makeurpicks.domain.PlayerBuilder;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = AuthServerApplication.class)
public class PlayerServiceTest {

	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Test
	public void createPlayer()
	{
		Player user = new PlayerBuilder("tim", "tdelesio@gmail.com", "123456").adAdmin().build();
		user = playerService.createPlayer(user);
		
		UserDetails player = playerService.loadUserByUsername(user.getUsername());
		
		Assert.assertEquals(user.getUsername(), player.getUsername());
		
		Assert.assertTrue(encoder.matches("123456", player.getPassword()));
		
	}
	
}

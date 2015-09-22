package com.makeurpicks.service;

import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.makeurpicks.PlayerApplication;
import com.makeurpicks.domain.Player;
import com.makeurpicks.domain.PlayerBuilder;
import com.makeurpicks.domain.User;
import com.makeurpicks.repository.PlayerByUsernameRepository;
import com.makeurpicks.repository.PlayerRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = PlayerApplication.class)
@IntegrationTest({ "server.port:0", "spring.cloud.config.enabled:true" })
@WebAppConfiguration
public class PlayerServiceTest {

	@Autowired
	protected PlayerService playerService;
	
	@Autowired
	protected PlayerRepository playerRepository;
	
	@Autowired
	protected PlayerByUsernameRepository playerByUserNameRepository;
	
	@Before
	public void setup()
	{
		playerRepository.deleteAll();
		
		playerByUserNameRepository.deleteAll();
	}
	
	@Test
	public void testLogin() {
		Player player = createPlayer("tdelesio@gmail.com", "tim", "password");
		
		User user = new User();
		user.setPassword(player.getPassword());
		user.setUsername(player.getUsername());
		player = playerService.login(user);
		Assert.assertNotNull(player.getId());
		
	}

//	@Test
//	public void testUpdatePassword() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testInitiateUpdatePasswordRequest() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testGetPlayerByUserName() {
		Player player = createPlayer("tdelesio@gmail.com", "tim", "password");
		
		Assert.assertEquals(player, playerByUserNameRepository.findOne(player.getUsername()));
	}

	@Test
	public void testRegister() {
		Player player = createPlayer("tdelesio@gmail.com", "tim", "password");
		
		Assert.assertEquals(player, playerRepository.findOne(player.getId()));			
	}
	
	private Player createPlayer(String email, String username, String password)
 	{
		Player player = new PlayerBuilder()
		.withEmail(email)
		.withPassword(password)
		.withUserName(username)
		.build();
		
		return playerService.register(player);
	}

}

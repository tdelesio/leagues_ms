package com.makeurpicks.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.makeurpicks.domain.Player.AccountLevels;

public class PlayerBuilder {

	private String username;
	private String email;
	private String password;
	private String refer;
	private String name;
	private String level;
	
//	private List<Authority> authorities;
	
	public PlayerBuilder(String u, String e, String p)
	{
		this.username = u;
		this.email = e;
		this.password = p;
		this.level = AccountLevels.user.toString();
	}
	

	public Player build()
	{
		Player player = new Player();
		player.setUsername(username);
		player.setEmail(email);
		player.setName(name);
		player.setRefer(refer);
		player.setPassword(password);
		player.setAccountLevel(level);
		
		return player;
	}
	
	public PlayerBuilder adAdmin()
	{
		this.level = AccountLevels.admin.toString();
		return this;
	}
}

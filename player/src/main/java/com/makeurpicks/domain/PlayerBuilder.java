package com.makeurpicks.domain;

import java.util.UUID;

import com.makeurpicks.domain.Player.MemberLevel;
import com.makeurpicks.domain.Player.PlayerStatus;

public class PlayerBuilder {

	private String id;
	private long facebookId;
	private PlayerStatus status = PlayerStatus.ACTIVE;
	private MemberLevel memberLevel = MemberLevel.USER;
	private String username;
	private String password;
	private String token;
	private String email;
	
	public PlayerBuilder()
	{
		UUID uuid = UUID.randomUUID();
		this.id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
	}
	
	public PlayerBuilder(String id)
	{
		this.id = id;
	}
	
	public Player build()
	{
		Player player = new Player();
		player.setId(id);
		player.setFacebookId(facebookId);
		player.setMemberLevel(memberLevel);
		player.setStatus(status);
		player.setEmail(email);
		player.setPassword(password);
		player.setUsername(username);
		player.setToken(token);
		
		return player;
	}
	
	public PlayerBuilder withEmail(String e)
	{
		this.email = e;
		return this;
	}
	
	public PlayerBuilder asAdmin()
	{
		this.memberLevel = MemberLevel.ADMIN;
		return this;
	}
	
	public PlayerBuilder withPassword(String p)
	{
		this.password = p;
		return this;
	}
	
	public PlayerBuilder withUserName(String u)
	{
		this.username = u;
		return this;
	}
}

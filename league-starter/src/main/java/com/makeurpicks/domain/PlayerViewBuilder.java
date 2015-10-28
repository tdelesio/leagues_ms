package com.makeurpicks.domain;

import java.util.UUID;

import com.makeurpicks.domain.PlayerView.MemberLevel;
import com.makeurpicks.domain.PlayerView.PlayerStatus;

public class PlayerViewBuilder {

	private String id;
	private long facebookId;
	private PlayerStatus status = PlayerStatus.ACTIVE;
	private MemberLevel memberLevel = MemberLevel.USER;
	private String username;
	private String password;
	private String token;
	private String email;
	
	public PlayerViewBuilder()
	{
		UUID uuid = UUID.randomUUID();
		this.id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
	}
	
	public PlayerViewBuilder(String id)
	{
		this.id = id;
	}
	
	public PlayerView build()
	{
		PlayerView player = new PlayerView();
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
	
	public PlayerViewBuilder withEmail(String e)
	{
		this.email = e;
		return this;
	}
	
	public PlayerViewBuilder asAdmin()
	{
		this.memberLevel = MemberLevel.ADMIN;
		return this;
	}
	
	public PlayerViewBuilder withPassword(String p)
	{
		this.password = p;
		return this;
	}
	
	public PlayerViewBuilder withUserName(String u)
	{
		this.username = u;
		return this;
	}
}

package com.makeurpicks.player;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class PlayerView implements UserDetails {


	private String username;
	
	private String email;
	private String password;
	private String refer;
	private String name;
	private boolean enabled=true;
	private boolean accountNonLocked=true;
	
	public enum AccountLevels {user, admin}
	
	private String accountLevel = "user";
	
//	@OneToMany(cascade=CascadeType.ALL, mappedBy="authority")
	private List<GrantedAuthority> authorities;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}
	
	
	



	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRefer() {
		return refer;
	}

	public void setRefer(String refer) {
		this.refer = refer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public String getAccountLevel() {
		return accountLevel;
	}

	public void setAccountLevel(String accountLevel) {
		this.accountLevel = accountLevel;
	}






	@Override
	public String toString() {
		return "PlayerView [username=" + username + ", email=" + email + ", password=" + password + ", refer=" + refer
				+ ", name=" + name + ", enabled=" + enabled + ", accountNonLocked=" + accountNonLocked
				+ ", accountLevel=" + accountLevel + ", authorities=" + authorities + "]";
	}
	
	
	
	
}

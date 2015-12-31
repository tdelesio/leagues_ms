package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.makeurpicks.dao.PlayerDao;
import com.makeurpicks.domain.Player;
import com.makeurpicks.exception.PlayerValidationException;
import com.makeurpicks.exception.PlayerValidationException.PlayerExceptions;

@Service
public class PlayerService implements UserDetailsService {

	@Autowired
	private PlayerDao playerDao;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Player player = playerDao.findByUsername(username);
		if (player == null) 
			return null;
		
		List<GrantedAuthority> auth = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		if (player.getAccountLevel().equals("admin")) {
			auth = AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN");
					
		}		
		
		player.setAuthorities(auth);
		
		return player;
	
	}
	
	
	public Player createPlayer(Player player)
	{
		validatePlayer(player);
		
		String encodedPassword = passwordEncoder.encode(player.getPassword());
		player.setPassword(encodedPassword);
		
		playerDao.save(player);
		
		return player;
	}
	
	private void validatePlayer(Player player)
	{
		List<PlayerExceptions> codes = new ArrayList<PlayerExceptions>();
		
		if (player == null)
			throw new PlayerValidationException();
		
		if (player.getUsername()==null ||"".equals(player.getUsername()))
			codes.add(PlayerExceptions.USERNAME_IS_NULL);
		
		if (player.getEmail()==null || "".equalsIgnoreCase(player.getEmail()))
			codes.add(PlayerExceptions.EMAIL_IS_NULL);
		
		if (player.getPassword()==null || "".equalsIgnoreCase(player.getPassword()))
			codes.add(PlayerExceptions.PASSWORD_DOES_NOT_MEET_REQ);
		
				
		if (!codes.isEmpty())
			throw new PlayerValidationException(codes.toArray(new PlayerExceptions[codes.size()]));
	}

}

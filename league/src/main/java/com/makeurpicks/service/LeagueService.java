package com.makeurpicks.service;

import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueName;
import com.makeurpicks.domain.PlayerLeague;
import com.makeurpicks.exception.LeagueServerException;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.exception.LeagueValidationException.LeagueExceptions;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.LeaguesAPlayHasJoinedRespository;
import com.makeurpicks.repository.PlayersInLeagueRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class LeagueService {

	@Autowired
	private LeagueRepository leagueRepository;

	@Autowired
	private PlayersInLeagueRepository playersInLeagueRepository;

	@Autowired
	private LeaguesAPlayHasJoinedRespository leaguesPlayerHasJoinedRepository;
	
	public League createLeague(League league) throws LeagueValidationException {
		validateLeague(league);

		
		String id = UUID.randomUUID().toString();
		
		league.setId(id);
		leagueRepository.save(league);

		addPlayerToLeague(league, league.getAdminId());
		
		return league;
	}

	public League updateLeague(League league) throws LeagueValidationException {
		validateLeague(league);

		leagueRepository.save(league);
		
		return league;
	}

	public Set<LeagueName> getLeaguesForPlayer(String playerId) throws LeagueValidationException {
		return leaguesPlayerHasJoinedRepository.findOne(playerId).getLeauges();
		
	}
	
	public Set<String> getPlayersInLeague(String leagueid) throws LeagueValidationException {
		return playersInLeagueRepository.findOne(leagueid).getPlayers();
		
	}

	public void joinLeague(PlayerLeague playerLeague)
	{
		if (playerLeague.getLeagueId() == null)
		{
			if (playerLeague.getLeagueName()==null)
			{
				throw new LeagueValidationException(LeagueExceptions.LEAGUE_NOT_FOUND);
			}
			else
			{
				League league = getLeagueByName(playerLeague.getLeagueName());
				if (league == null)
					throw new LeagueValidationException(LeagueExceptions.LEAGUE_NOT_FOUND);
				playerLeague.setLeagueId(league.getId());
			}
		}
		
		joinLeague(playerLeague.getLeagueId(), playerLeague.getPlayerId(), playerLeague.getPassword());
	}

	protected void joinLeague(String leagueId, String playerId, String password) throws LeagueValidationException {
		
//		if (!isValidPlayer(playerId))
//			throw new LeagueValidationException(LeagueExceptions.PLAYER_NOT_FOUND);
		
		League league = leagueRepository.findOne(leagueId);
		if (league == null)
			throw new LeagueValidationException(LeagueExceptions.LEAGUE_NOT_FOUND);
		
		if (league.getPassword()!=null&&!"".equals(league.getPassword())&& !league.getPassword().equals(password))
			throw new LeagueValidationException(LeagueExceptions.INVALID_LEAGUE_PASSWORD);
		
		addPlayerToLeague(league, playerId);

	}
	
	protected void addPlayerToLeague(League league, String playerId)
	{
//		PlayerResponse playerResponse = getPlayer(playerId);
		playersInLeagueRepository.addPlayerToLeague(playerId, league.getId());
		leaguesPlayerHasJoinedRepository.addPlayerToLeague(league, playerId);
	}

	public League getLeagueById(String leagueId) {
		return leagueRepository.findOne(leagueId);
	}

	public League getLeagueByName(String name) {
		Iterable<League> leagues = leagueRepository.findAll();
		for (League league:leagues)
		{
			if (league.getLeagueName().equals(name))
				return league;
		}
		
		return null;
	}

	public void removePlayerFromLeagye(String leagueId, String playerId) {
		League league = leagueRepository.findOne(leagueId);
		if (league == null)
			throw new LeagueValidationException(LeagueExceptions.LEAGUE_NOT_FOUND);
		
//		PlayerResponse playerResponse = getPlayer(playerId);
		playersInLeagueRepository.delete(league.getId());
		leaguesPlayerHasJoinedRepository.delete(league.getId());
	}

	/*
	 * 
	 * 
	 */
	protected void validateLeague(League league) throws LeagueValidationException {
		if (league.getLeagueName() == null || league.getLeagueName().equals(""))
			throw new LeagueValidationException(
					LeagueExceptions.LEAGUE_NAME_IS_NULL);
		if (getLeagueByName(league.getLeagueName()) != null)
			throw new LeagueValidationException(
					LeagueExceptions.LEAGUE_NAME_IN_USE);
		if (league.getSeasonId().isEmpty())
			throw new LeagueValidationException(
					LeagueExceptions.SEASON_ID_IS_NULL);

		if (league.getAdminId().isEmpty())
			throw new LeagueValidationException(
					LeagueExceptions.ADMIN_NOT_FOUND);

		if (!isValidPlayer(league.getAdminId()))
			throw new LeagueValidationException(
					LeagueExceptions.ADMIN_NOT_FOUND);
	}

	private boolean isLeagueValid(String leagueId) {
		League league = leagueRepository.findOne(leagueId);
		if (league == null)
			return false;
		else
			return true;
	}

	
	protected boolean isValidPlayer(String playerId) {
		// InstanceInfo instance =
		// discoveryClient.getNextServerFromEureka("player", false);
//		URI uri = UriComponentsBuilder.fromHttpUrl(instance.getHomePageUrl())
//				.path("player").path("/id/").path(playerId).build().encode()
//				.toUri();

		
//		ServiceInstance instance = loadBalancer.choose("player");
//		URI uri = UriComponentsBuilder.fromHttpUrl(String.format("http://%s:%d",
//				instance.getHost(), instance.getPort()))
//				.path("player").path("/id/").path(playerId).build().encode()
//				.toUri();
				

//		RestTemplate restTemplate = new RestTemplate();
//		
//		
//		PlayerResponse response = restTemplate.getForObject(uri,
//				PlayerResponse.class);

//		PlayerResponse response = getPlayer(playerId);
//		if (response != null && response.getId() != null)
			return true;
//		else
//			return false;

	}
	
	
	public Iterable<League> getAllLeagues()
	{
		return leagueRepository.findAll();
	}

//	public void setPlayerClient(PlayerClient playerClient) {
//		this.playerClient = playerClient;
//	}

	public void deleteLeague(String leagueId)
	{
		Set<String> playerIds = getPlayersInLeague(leagueId);
		for (String playerId:playerIds)
		{
			try {removePlayerFromLeagye(leagueId, playerId);} catch (Exception e) {e.getMessage();}
		}
		
		leagueRepository.delete(leagueId);
	}
}

package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.makeurpicks.domain.League;
import com.makeurpicks.domain.LeagueType;
import com.makeurpicks.domain.PlayerResponse;
import com.makeurpicks.domain.Season;
import com.makeurpicks.exception.LeagueServerException;
import com.makeurpicks.exception.LeagueValidationException;
import com.makeurpicks.exception.LeagueValidationException.LeagueExceptions;
import com.makeurpicks.repository.LeagueRepository;
import com.makeurpicks.repository.SeasonRepository;
import com.makeurpicks.repository.redis.RedisLeaguesPlayerHasJoinedRepository;
import com.makeurpicks.repository.redis.RedisPlayersInLeagueRespository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class LeagueService {

	@Autowired
	private LeagueRepository leagueRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RedisPlayersInLeagueRespository playersInLeagueRespository;

	@Autowired
	private RedisLeaguesPlayerHasJoinedRepository leaguesPlayerHasJoinedRepository;

	@Autowired 
	private SeasonRepository seasonRepository;
	
	// @Autowired
	// private DiscoveryClient discoveryClient;

//	@Autowired
//	private LoadBalancerClient loadBalancer;

	@Autowired
	private PlayerClient playerClient;
	
	public League createLeague(League league) throws LeagueValidationException {
		validateLeague(league);

		UUID uuid = UUID.randomUUID();
		String id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
		
		league.setId(id);
		leagueRepository.save(league);

		addPlayerToLeague(league.getId(), league.getAdminId());
		
		return league;
	}

	public League updateLeague(League league) throws LeagueValidationException {
		validateLeague(league);

		leagueRepository.save(league);
		
		return league;
	}

	public List<League> getLeaguesForPlayer(String playerId) throws LeagueValidationException {
		Set<String> leagueIds = leaguesPlayerHasJoinedRepository.findOne(playerId).getList();
		List<League> leagues = new ArrayList<League>();
		for (String id:leagueIds)
		{
			League league = getLeagueById(id); 
			if (league == null)
				throw new LeagueValidationException(LeagueExceptions.LEAGUE_NOT_FOUND);
			
			leagues.add(league);
		}
		
		return leagues;
	}
	

	public void joinLeague(String leagueId, String playerId, String password) throws LeagueValidationException {
		
		if (!isValidPlayer(playerId))
			throw new LeagueValidationException(LeagueExceptions.PLAYER_NOT_FOUND);
		
		League league = leagueRepository.findOne(leagueId);
		if (league == null)
			throw new LeagueValidationException(LeagueExceptions.LEAGUE_NOT_FOUND);
		
		if (!league.getPassword().equals(password))
			throw new LeagueValidationException(LeagueExceptions.INVALID_LEAGUE_PASSWORD);
		
		addPlayerToLeague(leagueId, playerId);

	}
	
	protected void addPlayerToLeague(String leagueId, String playerId)
	{
		playersInLeagueRespository.addPlayerToLeague(leagueId, playerId);
		leaguesPlayerHasJoinedRepository.addPlayerToLeague(leagueId, playerId);
	}

	public League getLeagueById(String leagueId) {
		return leagueRepository.findOne(leagueId);
	}

	public League getLeagueByName(String name) {
		return null;
	}

	public void removePlayerFromLeagye(String leagueId, String playerId) {
		playersInLeagueRespository.removePlayerFromLeague(leagueId, playerId);
		leaguesPlayerHasJoinedRepository.removePlayerFromLeague(leagueId, playerId);
	}

	/*
	 * 
	 * 
	 */
	private void validateLeague(League league) throws LeagueValidationException {
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
	
	protected boolean defaultIsValidPlayer(String playerId)
	{
		return false;
	}
	
	@HystrixCommand(fallbackMethod="defaultIsValidPlayer")
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

		PlayerResponse response = playerClient.getPlayerById(playerId);
		if (response != null && response.getId() != null)
			return true;
		else
			return false;

	}

	public Iterable<League> defaultLeaguesDown()
	{
		throw new LeagueServerException();
	}
	
	
	@HystrixCommand(fallbackMethod="defaultLeaguesDown")
	public Iterable<League> getAllLeagues()
	{
		return leagueRepository.findAll();
	}

	public void setPlayerClient(PlayerClient playerClient) {
		this.playerClient = playerClient;
	}
	
	
	public List<Season> getCurrentSeasons()
	{
		List<Season> s = new ArrayList<Season>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		int currentYear = calendar.get(Calendar.YEAR);
		
		for (LeagueType lt : LeagueType.values())
		{
			Iterable<Season> seasons = seasonRepository.getSeasonsByLeagueType(lt.toString());
			for (Season season:seasons)
			{
				if (season.getStartYear() >= currentYear)
					s.add(season);
			}
		}
		
		return s;
	}
	
	public Season createSeason(Season season)
	{
		UUID uuid = UUID.randomUUID();
		String id = String.valueOf(uuid.getMostSignificantBits())+String.valueOf(uuid.getLeastSignificantBits());
		
		season.setId(id);
		
		return seasonRepository.createUpdateSeason(season);
	}
	
	public Season updateSeason(Season season)
	{
		return seasonRepository.createUpdateSeason(season);
	}
	
	public LeagueType[] getLeagueType()
	{
		return LeagueType.values();
	}
}

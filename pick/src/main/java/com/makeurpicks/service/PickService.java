package com.makeurpicks.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.makeurpicks.domain.DoublePick;
import com.makeurpicks.domain.GameResponse;
import com.makeurpicks.domain.LeagueResponse;
import com.makeurpicks.domain.Pick;
import com.makeurpicks.exception.PickValidationException;
import com.makeurpicks.exception.PickValidationException.PickExceptions;
import com.makeurpicks.repository.DoublePickRepository;
import com.makeurpicks.repository.PickRepository;
import com.makeurpicks.repository.PicksByLeagueWeekAndPlayerRepository;
import com.makeurpicks.repository.PicksByLeagueWeekRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class PickService {

	@Autowired
	private PickRepository pickRepository;
	
	@Autowired
	private GameClient gameClient;
	
	@Autowired 
	private LeagueClient leagueClient;
	
	@Autowired
	private PicksByLeagueWeekRepository picksByLeagueWeekRepository;
	
	@Autowired
	private PicksByLeagueWeekAndPlayerRepository picksByLeagueWeekAndPlayerRepository;
	
	@Autowired
	private DoublePickRepository doublePickRepository;
	
	
	
	public void setGameClient(GameClient gameClient) {
		this.gameClient = gameClient;
	}

	
	
	public void setLeagueClient(LeagueClient leagueClient) {
		this.leagueClient = leagueClient;
	}



	public Pick makePick(Pick pick)
	{
		//make sure all the parms are set
		validatePick(pick);  
		
		//save pick by pick id
		pickRepository.save(pick);
		
		//save the pick so it can be assessed by league and week
		picksByLeagueWeekRepository.addPick(pick);
		
		//save the pick so it can be assessed by league, week, and player
		picksByLeagueWeekAndPlayerRepository.addPick(pick);
		
		return pick;
	}


	public Pick updatePick(Pick pick, String loggedInPlayerId)
	{
		//make sure all the parms are set
		validatePick(pick);  
		
		if (pick.getPlayerId() != loggedInPlayerId)
			throw new PickValidationException(PickExceptions.UNAUTHORIZED_USER);
		
		//save pick by pick id
		pickRepository.save(pick);
		
		return pick;
	}
	
	public Iterable<Pick> getPicksByLeagueAndWeek(String leagueId, String weekId)
	{ 
		Iterable<String> ids = picksByLeagueWeekRepository.getPicksForLeagueAndWeek(leagueId, weekId);
		return pickRepository.findAll(ids);
	}
	
	public Iterable<Pick> getPicksByLeagueWeekAndPlayer(String leagueId, String weekId, String playerId)
	{ 
		Iterable<String> ids = picksByLeagueWeekAndPlayerRepository.getPicksForLeagueWeekAndPlayer(leagueId, weekId, playerId);
		return pickRepository.findAll(ids);
	}
	
	private List<LeagueResponse> defaultGetLeaguesForPlayer(String playerId)
	{
		throw new PickValidationException(PickExceptions.LEAGUE_SERVICE_IS_DOWN);
	}
	
	@HystrixCommand(fallbackMethod="defaultGetLeaguesForPlayer")
	private List<LeagueResponse> getLeaguesForPlayer(String playerId) 
	{
		return leagueClient.getLeaguesForPlayer(playerId);
	}
	
	private GameResponse defaultGetGameById(String gameId)
	{
		throw new PickValidationException(PickExceptions.GAME_SERVICE_IS_DOWN);
	}
	
	@HystrixCommand(fallbackMethod="defaultGetGameById")
	private GameResponse getGameById(String gameId)
	{
		return gameClient.getGameById(gameId);
	}
	
	private void validatePick(Pick pick)
	{
		List<PickExceptions> codes = new ArrayList<PickExceptions>();
		
		
		if (pick == null)
		{
			throw new PickValidationException(PickExceptions.PICK_IS_NULL);
		}
		
		if ("".equals(pick.getGameId()))
			codes.add(PickExceptions.GAME_IS_NULL);
		
		if ("".equals(pick.getTeamId()))
			codes.add(PickExceptions.TEAM_IS_NULL);
		
		if ("".equals(pick.getWeekId()))
			codes.add(PickExceptions.WEEK_IS_NULL);
		
		if ("".equals(pick.getLeagueId()))
			codes.add(PickExceptions.LEAGUE_IS_NULL);
		
		if (pick.getLeagueId()==null)
			codes.add(PickExceptions.LEAGUE_IS_NULL);
		
		if ("".equals(pick.getPlayerId()))
			codes.add(PickExceptions.PLAYER_IS_NUll);
		
		
		GameResponse game = getGameById(pick.getGameId());
//		Game game = dao.loadByPrimaryKey(Game.class, pick.getGame().getId());
		if (game == null)
			codes.add(PickExceptions.GAME_IS_NULL);
		
		//load the game to make sure that the team passed is actually playing in the game
//		if (game.getFav().getId()!=pick.getTeam().getId() && game.getDog().getId()!=pick.getTeam().getId())
		if (game.getFavoriteTeamId()!=pick.getTeamId() && game.getDogTeamId()!=pick.getTeamId())
			codes.add(PickExceptions.TEAM_NOT_PLAYING_IN_GAME);
				
		//check to make sure that the game hasn't started
		if (game.hasGameStarted())
			codes.add(PickExceptions.GAME_HAS_ALREADY_STARTED);
		
		//need to make sure that the user is in that league
//		leagueManager.verifyPlayerExistsInLeague(pick.getLeague().getId(), pick.getName().getId());
		List<LeagueResponse> leagues = getLeaguesForPlayer(pick.getPlayerId());
		boolean playerExistsInLeague = false;
		for (LeagueResponse league: leagues)
		{
			if (league.getId().equals(pick.getLeagueId()))
			{
				playerExistsInLeague = true;
				break;
			}
		}
		
		if (!playerExistsInLeague)
			codes.add(PickExceptions.PLAYER_NOT_IN_LEAGUE);
		
		//make sure the week matches the game
//		if (game.getWeek().getId()!=pick.getWeek().getId())
		if (game.getWeekId()!=pick.getWeekId())
			codes.add(PickExceptions.WEEK_IS_NOT_VALID);
				
		if (!codes.isEmpty())
			throw new PickValidationException(codes.toArray(new PickExceptions[codes.size()]));
	}
	
	
	public DoublePick getDoublePick(String leagueId, String weekId, String playerId)
	{
		return doublePickRepository.findOne(DoublePick.buildString(leagueId, weekId, playerId));
	}

	public DoublePick makeDoublePick(String pickId, String loggedInPlayerId)
	{
//		Picks pick = dao.loadByPrimaryKey(Picks.class, pickId);
		Pick pick = pickRepository.findOne(pickId);
		if (pick==null)
			throw new PickValidationException(PickExceptions.PICK_IS_NULL);
		
		GameResponse game = gameClient.getGameById(pick.getGameId());
		//check to see if the newly selected double has started
		if (game.hasGameStarted())
		{
			//you can't change to a game that has started
			throw new PickValidationException(PickExceptions.GAME_HAS_ALREADY_STARTED);
		}
		
		if (!pick.getPlayerId().equals(loggedInPlayerId))
			throw new PickValidationException(PickExceptions.UNAUTHORIZED_USER);
		
//		Picks oldPick = getDoublePickForPlayerLeagueAndWeek(pick.getName(), pick.getLeague(), pick.getWeek());
		DoublePick orginialDoublePick = doublePickRepository.findOne(DoublePick.buildString(pick.getLeagueId(), pick.getWeekId(), pick.getPlayerId())); 
		
		//need to check to see if the orginal pick game has started
		if (orginialDoublePick!=null)
		{
			String orginalPickId = orginialDoublePick.getPickId();
			Pick orginalPick = pickRepository.findOne(orginalPickId);
			GameResponse orginalGame = gameClient.getGameById(orginalPick.getGameId());
			if (orginalGame.hasGameStarted())
			{
				throw new PickValidationException(PickExceptions.GAME_HAS_ALREADY_STARTED);
			}
			
			//update the to the new pick and update the repo
			orginialDoublePick.setPickId(pickId);
			doublePickRepository.save(orginialDoublePick);
			
			return orginialDoublePick;
		}
		else
		{
			//there is no orginal pick, so create a new one
			DoublePick doublePick = new DoublePick(pick.getLeagueId(), pick.getWeekId(), pick.getPlayerId());
			doublePick.setPickId(pickId);
			
			doublePickRepository.save(doublePick);
			
			return doublePick;
		}
		
		
		
	}
}

package com.makeurpicks.repository.redis;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.makeurpicks.domain.Game;
import com.makeurpicks.repository.GameRepository;

public class RedisGameRepository extends AbstractRedisSetCRUDRepository<Game>
		implements GameRepository {

	public final static String key_prefix = "GAMES_BY_WEEK-";
	public final static String GAME_BUCKET = "GAMES";
	
	protected final HashOperations<String, String, Game> hashOps;
	
	public RedisGameRepository(RedisTemplate<String, Game> redisTemplate)
	{
		super(redisTemplate);
		this.hashOps = redisTemplate.opsForHash();
	}
	
	
	
	
	
	private String buildKey(String weekId)
	{
		return key_prefix+weekId;
	}
	
	public Game getGameById(String gameId)
	{
		return hashOps.get(GAME_BUCKET, gameId);
	}
	public Game createUpdateGame(Game game)
	{
		add(buildKey(game.getWeekId()), game);
		
		hashOps.put(GAME_BUCKET, game.getId(), game);
		return game;
	}
	
	public Iterable<Game> getGamesByWeek(String weekId)
	{
		return findAll(buildKey(weekId));
	}
	

	
}

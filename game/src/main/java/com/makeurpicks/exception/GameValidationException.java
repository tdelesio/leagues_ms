package com.makeurpicks.exception;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameValidationException extends RuntimeException {

	public enum GameExceptions {
		GAME_IS_NULL, FAVORITE_IS_NULL, DOG_IS_NULL, WEEK_IS_NULL, GAMESTART_IS_NULL, TEAM_CANNOT_PLAY_ITSELF
	}
	
	private Iterable<GameExceptions> exceptions;
	
	public GameValidationException(GameExceptions... gameExceptions)
	{
		exceptions = new ArrayList<GameExceptions>(Arrays.asList(gameExceptions));
	}
	
	public boolean hasException()
	{
		if (exceptions.iterator().hasNext())
			return false;
		else
			return true;
	}

	@Override
	public String getMessage() {
		return exceptions.toString();
	}
	
}

package com.makeurpicks.exception;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerValidationException extends RuntimeException {

	public enum PlayerExceptions {USER_NOT_FOUND, PASSWORD_INCORRECT, USERNAME_TAKE, ACCOUNT_DISABLED,
		PLAYER_IS_NULL, USERNAME_IS_NULL, PASSWORD_IS_NULL, EMAIL_IS_NULL}
	
	private Iterable<PlayerExceptions> exceptions;
	
	public PlayerValidationException(PlayerExceptions... pickExceptions)
	{
		exceptions = new ArrayList<PlayerExceptions>(Arrays.asList(pickExceptions));
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

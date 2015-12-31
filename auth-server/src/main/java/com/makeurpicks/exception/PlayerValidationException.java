package com.makeurpicks.exception;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerValidationException extends RuntimeException {

	public enum PlayerExceptions {EMAIL_IS_NULL, USERNAME_IS_NULL, PASSWORD_DOES_NOT_MEET_REQ, USERNAME_TAKEN}
	
	private Log log = LogFactory.getLog(PlayerValidationException.class);
	
	
	private Iterable<PlayerExceptions> exceptions;
	
	public PlayerValidationException(PlayerExceptions... PlayerExceptions)
	{
		log.debug(PlayerExceptions);
		exceptions = new ArrayList<PlayerExceptions>(Arrays.asList(PlayerExceptions));
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

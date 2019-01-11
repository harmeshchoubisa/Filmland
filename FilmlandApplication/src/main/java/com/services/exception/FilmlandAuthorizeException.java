package com.services.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class FilmlandAuthorizeException extends RuntimeException {

	public FilmlandAuthorizeException(String exception) {
		super(exception);
	}

}

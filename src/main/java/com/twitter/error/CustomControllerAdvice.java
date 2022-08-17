package com.twitter.error;

import java.util.List;
import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.exceptions.UnauthorizedTweetDeletionException;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


@ControllerAdvice
public class CustomControllerAdvice {

	@ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Error handleMethodArgumentNotValidException(Exception ex, WebRequest request) {
		if(ex instanceof MethodArgumentNotValidException) {
		return new Error(HttpStatus.BAD_REQUEST.value(), 6, ((MethodArgumentNotValidException) ex).getFieldError().getDefaultMessage());
		}
		else if (ex instanceof ConstraintViolationException) {
			List<String> errors = new ArrayList<>();

			((ConstraintViolationException)ex).getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

			return new Error(HttpStatus.BAD_REQUEST.value(), 2, errors.get(0));
		}
		return new Error (HttpStatus.BAD_REQUEST.value(), 1, "Bad request");
		}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Error handleMissingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
		return new Error(HttpStatus.UNAUTHORIZED.value(), 1, ex.getMessage());
	}

	@ExceptionHandler(TweetNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Error handleTweetNotFoundException(TweetNotFoundException ex, WebRequest request) {
	   return new Error(HttpStatus.NOT_FOUND.value(), 1, ex.getMessage());
	    
	}
	
	@ExceptionHandler(UnauthorizedTweetDeletionException.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Error handleUnauthorizedTweetDeletionExceptiom(UnauthorizedTweetDeletionException ex, WebRequest request) {
	   return new Error(HttpStatus.FORBIDDEN.value(), 1, ex.getMessage());
	    
	}
	
	
	
}

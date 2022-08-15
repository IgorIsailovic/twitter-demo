package com.igor.igor.error;

import java.util.List;
import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;


@ControllerAdvice
public class CustomControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
		return new Error(HttpStatus.BAD_REQUEST.value(), 6, ex.getFieldError().getDefaultMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Error constraintViolationException(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();

		ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

		return new Error(HttpStatus.BAD_REQUEST.value(), 1, errors.get(0));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public Error handleMissingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
		return new Error(HttpStatus.UNAUTHORIZED.value(), 1, ex.getMessage());
	}

	@ExceptionHandler(TweetNotFoundException.class)
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ResponseBody
	public Error handleTweetNotFoundException(TweetNotFoundException e) {
	   return new Error(HttpStatus.NOT_FOUND.value(), 1,"Specified tweet does not exist!");
	    
	}
	
	@ExceptionHandler(UnauthorizedTweetDeletionExceptiom.class)
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	@ResponseBody
	public Error handleUnauthorizedTweetDeletionExceptiom(UnauthorizedTweetDeletionExceptiom e) {
	   return new Error(HttpStatus.FORBIDDEN.value(), 1,"Only allowed to delete own tweets!");
	    
	}
	
	
	
}

package com.igor.igor.error;

import java.util.List;
import java.util.ArrayList;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomControllerAdvice {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), 6, ex.getFieldError().getDefaultMessage());
	}

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorResponse constraintViolationException(ConstraintViolationException ex, WebRequest request) {
		List<String> errors = new ArrayList<>();

		ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));

		return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), 1, errors.get(0));
	}

	@ExceptionHandler(MissingRequestHeaderException.class)
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	@ResponseBody
	public ErrorResponse handleMissingRequestHeaderException(MissingRequestHeaderException ex, WebRequest request) {
		return new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), 1, ex.getMessage());
	}

}

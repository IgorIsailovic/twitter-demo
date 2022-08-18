package com.twitter.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.models.PostTweetReq;
import com.twitter.models.TweetResp;
import com.twitter.models.TweetsPageResp;
import com.twitter.service.TweetService;

import io.swagger.v3.oas.annotations.Operation;

@Validated
@RestController
@RequestMapping("/v1/tweets")
public class TweetController {

	private TweetService tweetService;

	public TweetController(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	@Operation(summary = "Post a tweet to the service")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResp createTweet(@Valid @RequestBody PostTweetReq postTweetReq,
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername) {
		
		return tweetService.createTweet(xUsername, postTweetReq);
	}

	@Operation(summary = "Queries the tweets, returning a page of tweets that match the provided query params sorted by the time created. " + 
	"Multiple query params for hash tags and usernames could be specified. If that is the case, tweets that have at least one of the specified hash tags match the query. Same goes for username.")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public TweetsPageResp getAllUsers(
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow the pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername,
			@RequestParam(required = false) List<String> hashTag, 
			@RequestParam(required = false) List<String> username,
			@RequestParam(defaultValue = "50") @Min(value = 1, message = "Limit param must be greater than 0") @Max(value = 100, message = "Limit param must not be greater than 100") int limit,
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Offset parametar must be greater or equal to 0") int offset,
			HttpServletRequest request) {
		
		return tweetService.getTweets(offset, limit, hashTag, username, request);

	}

	@Operation(summary = "Deletes a tweet from the system. User can only "+ 
	"delete his own tweets (username must match).")
	@DeleteMapping(value = "/{tweetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public TweetResp deleteTweet(
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername,
			@PathVariable("tweetId") String id) {

		return tweetService.deleteTweet(id, xUsername);

	}

}

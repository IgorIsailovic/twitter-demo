package com.igor.igor.controller;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.igor.igor.error.UnauthorizedTweetDeletionExceptiom;
import com.igor.igor.models.PostTweetReq;
import com.igor.igor.models.TweetResp;
import com.igor.igor.service.implementation.TweetRespServiceImpl;
import com.igor.igor.util.TweetsPageResp;
import com.igor.igor.util.Util;

import io.swagger.v3.oas.annotations.Operation;

@Validated
@RestController
@RequestMapping("/v1/tweets")
public class TweetRespController {

	
	@Autowired
	private TweetRespServiceImpl tweetService;

	@Operation(summary = "Post a tweet to the service")
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public TweetResp createTweet(@Valid @RequestBody PostTweetReq postTweetReq,
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername) {
		TweetResp tweet = new TweetResp();
		tweet.setCreatedBy(xUsername);
		tweet.setTweetBody(postTweetReq.getTweetBody());
		tweet.setHashtags(postTweetReq.getHashtags());
		tweetService.createTweet(tweet);
		return tweet;
	}

	@Operation(summary = "Queries the tweets, returning a page of tweets that match the provided query params sorted by the time created. Multiple query params for hash tags and usernames could be specified. If that is the case, tweets that have at least one of the specified hash tags match the query. Same goes for username.")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public TweetsPageResp getAllUsers(
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername,
			@RequestParam(required = false) List<String> hashTag, @RequestParam(required = false) List<String> username,
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Offset parametar must be greater or equal to 0") int offset,
			@RequestParam(defaultValue = "50") @Min(value = 1, message = "Limit param must be greater than 0") @Max(value = 100, message = "Limit param must not be greater than 100") int limit,
			HttpServletRequest request) {

		TweetsPageResp response = new TweetsPageResp();
		Pageable paging = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
		Page<TweetResp> tweets; 
		Util util = new Util();

		
		
		if (username != null && hashTag != null) {
			tweets = tweetService.findByHashtagsInOrCreatedByIn(hashTag, username, paging);
		}
		else if (hashTag != null) {
			tweets = tweetService.findAllByHashtagsIn(hashTag, paging);
		}
		else if (username != null ) {
			tweets = tweetService.findAllByCreatedByIn(username, paging);
		}
		else tweets = tweetService.findAll(paging);

		
		if (tweets.isLast()) {
			response = new TweetsPageResp(tweets.getContent());
		} else {
			response = new TweetsPageResp(tweets.getContent(), util.uriFormater(request, paging, hashTag, username));
		}
		return response;

	}

	@Operation(summary = "Deletes a tweet from the system. User can only"
			+ " delete his own tweets (username must match).")
	@DeleteMapping(value = "/{tweetId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public TweetResp deleteTweet(@PathVariable("tweetId") String id,
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername) {

		TweetResp tweet = tweetService.findById(id);
		if (tweet.getCreatedBy().equals(xUsername)) {
			tweetService.deleteTask(id);
			return tweet;
		} else
			throw new UnauthorizedTweetDeletionExceptiom();

	}

		
}

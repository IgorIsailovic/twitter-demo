package com.igor.igor.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

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

import com.igor.igor.error.Error;
import com.igor.igor.error.TweetNotFoundException;
import com.igor.igor.error.UnauthorizedTweetDeletionExceptiom;
import com.igor.igor.models.PostTweetReq;
import com.igor.igor.models.TweetResp;
import com.igor.igor.service.implementation.TweetRespServiceImpl;
import com.igor.igor.util.TweetsPageResp;
import com.igor.igor.util.Util;

@Validated
@RestController
@RequestMapping("/v1/tweets")
public class TweetRespController {

	public TweetRespController(TweetRespServiceImpl tweetService) {
		this.tweetService = tweetService;
	}

	private TweetRespServiceImpl tweetService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public TweetsPageResp getAllUsers(
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername,
			@RequestParam(required = false) List<String> hashTag,
			@RequestParam(required = false) List<String> username,
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Offset parametar must be greater or equal to 0") int offset,
			@RequestParam(defaultValue = "50") @Min(value = 1, message = "Limit param must be greater than 0") @Max(value = 100, message = "Limit param must not be greater than 100") int limit,
			HttpServletRequest request) {

		List<TweetResp> tweets = new ArrayList<>();
		TweetsPageResp response = new TweetsPageResp();
		Pageable paging = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
		Util util = new Util();
		/*
		if (hashTag != null) {
			for (String hashTagg : hashTag) {
				tweetsHash.addAll(pageTweets.getContent().stream().filter(t -> t.getHashtags().contains(hashTagg))
						.collect(Collectors.toList()));
			}
		} else
			tweetsHash = pageTweets.getContent();

		System.out.println(paging.next());
		
		System.out.println(paging.getPageSize());
		System.out.println(tweetsHash.size());
*/
		if(username!=null) {
			for(String u : username) {
			tweets.addAll(tweetService.findByCreatedBy(u, paging).toList());
			}
		}
		if(hashTag!=null) {
			for(String h : hashTag) {
			tweets.addAll(tweetService.findByHashtags(h, paging).toList());
			}
		}
		if(username==null && hashTag == null) {
			tweets.addAll(tweetService.findAll(paging).getContent());
		}
		
		
		if (tweets.isEmpty() || tweets.size() < paging.getPageSize()) {
			response = new TweetsPageResp(tweets);
		} else {
			response = new TweetsPageResp(tweets, util.uriFormater(request, paging, hashTag, username));
		}
		return response;

	}

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
		//return new ResponseEntity<>(tweet, HttpStatus.CREATED);
	}

	@DeleteMapping(value="/{tweetId}" , produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public TweetResp deleteTweet(@PathVariable("tweetId") String id,
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String xUsername) {

		try {
			TweetResp tweet = tweetService.findById(id);
			if (tweet.getCreatedBy().equals(xUsername)) {
				tweetService.deleteTask(id);
				return tweet;
			} else {
						throw new UnauthorizedTweetDeletionExceptiom(); 
			}
	
		} catch (NoSuchElementException e) {
			throw new TweetNotFoundException();
		}
	}

}

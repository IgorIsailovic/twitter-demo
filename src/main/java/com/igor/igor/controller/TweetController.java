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
import org.springframework.web.bind.annotation.RestController;

import com.igor.igor.error.ErrorResponse;
import com.igor.igor.models.Tweet;
import com.igor.igor.service.implementation.TweetServiceImpl;
import com.igor.igor.util.TweetsPageResponse;
import com.igor.igor.util.Util;

@Validated
@RestController
@RequestMapping("/v1/tweets")
public class TweetController {

	public TweetController(TweetServiceImpl tweetService) {
		this.tweetService = tweetService;
	}

	private TweetServiceImpl tweetService;

	@GetMapping
	public TweetsPageResponse getAllUsers(
			@RequestParam(required = false) List<String> hashTag,
			@RequestParam(required = false) List<String> username,
			@RequestParam(defaultValue = "0") @Min(value = 0, message = "Offset parametar must be greater or equal to 0") int offset,
			@RequestParam(defaultValue = "50") @Min(value = 1, message = "Limit param must be greater than 0") @Max(value = 100, message = "Limit param must not be greater than 1000") int limit,
			HttpServletRequest request) {

		List<Tweet> tweets = new ArrayList<>();
		TweetsPageResponse response = new TweetsPageResponse();
		Pageable paging = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
		Util util = new Util();
		Page<Tweet> pageTweets = tweetService.findAll(paging);
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
			response = new TweetsPageResponse(tweets);
		} else {
			response = new TweetsPageResponse(tweets, util.urlFormater(request, paging, hashTag));
		}
		return response;

	}

	@PostMapping
	public ResponseEntity<?> createTweet(@Valid @RequestBody Tweet tweet,
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String username) {
		tweet.setCreatedBy(username);
		tweetService.createTweet(tweet);
		return ResponseEntity.created(null).body(tweet);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTweet(@PathVariable("id") String id,
			@RequestHeader(value = "X-Username", required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String username) {

		try {
			Tweet tweet = tweetService.findById(id);
			if (tweet.getCreatedBy().equals(username)) {
				tweetService.deleteTask(id);
				return ResponseEntity.ok().body(tweet);
			} else {
				return new ResponseEntity<>(
						new ErrorResponse(HttpStatus.FORBIDDEN.value(), 1, "Only allowed to delete own tweets"),
						HttpStatus.FORBIDDEN);
			}
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.value(), 1, "No tweet with id: " + id),
					HttpStatus.NOT_FOUND);
		}

	}

}

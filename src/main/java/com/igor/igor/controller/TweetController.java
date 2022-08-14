package com.igor.igor.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.igor.igor.models.Tweet;
import com.igor.igor.service.implementation.TweetServiceImpl;

@Validated
@RestController
@RequestMapping("/tweets")
public class TweetController {
	
	  public TweetController(TweetServiceImpl tweetService ) {
	    	this.tweetService = tweetService;
		}

	    private TweetServiceImpl tweetService;
	
	 @GetMapping
	 public List<Tweet> getAllUsers(@RequestParam(required = false) List<String> hashTag, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "3") int limit) {
		
		 List<Tweet> tweetsHash = tweetService.findAll();
		 if(hashTag!=null) {
		 	for(String hashTagg : hashTag) {
		 	 tweetsHash=tweetService.findAll().stream().filter(t -> t.getHashtags().contains(hashTagg)).collect(Collectors.toList());
		 	}
		 }
		 Pageable paging = PageRequest.of(offset, limit);
		 
		 Page<Tweet> pageTweets = tweetService.findAll(paging);
		 
	        return pageTweets.getContent();
		 
		
	    }
	 
	 
	 @PostMapping
	    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody Tweet tweet, @RequestHeader(value="X-Username",  required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String username ) {
		 try {
			/* if(tweet.getHashtags().size()>5) {
				 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			 }*/
			 if(!username.isEmpty()) tweet.setCreatedBy(username);
			 tweetService.createTweet(tweet);
	        return ResponseEntity.created(null).body(tweet);
		 }
		 catch (Exception e) {
			 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);		}
	    }


		 
	 
}


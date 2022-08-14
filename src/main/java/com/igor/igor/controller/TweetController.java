package com.igor.igor.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

import com.igor.igor.models.Tweet;
import com.igor.igor.service.implementation.TweetServiceImpl;
import com.igor.igor.util.TweetsPageResponse;
import com.igor.igor.util.Util;

@Validated
@RestController
@RequestMapping("/tweets")
public class TweetController {
	
	  public TweetController(TweetServiceImpl tweetService ) {
	    	this.tweetService = tweetService;
		}

	    private TweetServiceImpl tweetService;
	
	 @GetMapping
	 public TweetsPageResponse getAllUsers(@RequestParam(required = false) List<String> hashTag, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "3") int limit, HttpServletRequest request) {
		
		 List<Tweet> tweetsHash = new ArrayList<>();
		 
		 Pageable paging = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
		 
		 Page<Tweet> pageTweets = tweetService.findAll(paging);
		 
		 if(hashTag!=null) {
			 	for(String hashTagg : hashTag) {
			 	tweetsHash.addAll(pageTweets.getContent().stream().filter(t -> t.getHashtags().contains(hashTagg)).collect(Collectors.toList()));
			 	}
			 }
		 else tweetsHash=pageTweets.getContent();
		 
		System.out.println(paging.next());
		Util util = new Util();
		TweetsPageResponse response= new TweetsPageResponse();
		System.out.println(paging.getPageSize());
		System.out.println(tweetsHash.size());
		
		if(tweetsHash.isEmpty() || tweetsHash.size()<paging.getPageSize()) {
			 response = new TweetsPageResponse(tweetsHash);
		}
		else {
		  response = new TweetsPageResponse(tweetsHash, util.urlFormater(request, paging, hashTag) );
		}
	        return response;
		 
		
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

	 
	 @DeleteMapping("/{id}")
	 public ResponseEntity<String> deleteTweet(@PathVariable("id") String id, @RequestHeader(value="X-Username",  required = true) @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$", message = "X-username header must follow pattern: ^[a-zA-Z0-9_]{4,32}$") String username  ) {
		 if(tweetService.findById(id).getCreatedBy()==username) {
			 try{tweetService.deleteTask(id);
			 return ResponseEntity.ok().body(id);
			 }
			 catch (Exception e) {
					// TODO: handle exception
			}
		 }
		 if (currentPrincipalName.equals(username)) {
				return new ResponseEntity<>("Can't delete your self!",HttpStatus.BAD_REQUEST);//(HttpStatus.BAD_REQUEST, "Can't delete your self!");
			}
			if (userService.deleteUser(username).equals("OK"))
				return new ResponseEntity<>(HttpStatus.ACCEPTED);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

		 
	 
}


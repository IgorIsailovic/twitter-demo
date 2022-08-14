package com.igor.igor.util;

import java.net.URL;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.igor.igor.models.Tweet;

public class TweetsPageResponse {
	
	private List<Tweet> tweets;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private URL nextPage;
	
	public List<Tweet> getTweets() {
		return tweets;
	}
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	public URL getNextPage() {
		return nextPage;
	}
	public void setNextPage(URL nextPage) {
		this.nextPage = nextPage;
	}
	
	public TweetsPageResponse(List<Tweet> tweets, URL nextPage) {
		super();
		this.tweets = tweets;
		this.nextPage = nextPage;
	}
	
	public TweetsPageResponse(List<Tweet> tweets) {
		super();
		this.tweets = tweets;
		
	}
	
	public TweetsPageResponse() {
	}
	
	
	

}

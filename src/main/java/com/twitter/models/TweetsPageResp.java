package com.twitter.models;

import java.net.URI;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TweetsPageResp {
	
	private List<TweetResp> tweets;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private URI nextPage;
	
	public List<TweetResp> getTweets() {
		return tweets;
	}
	public void setTweets(List<TweetResp> tweets) {
		this.tweets = tweets;
	}
	public URI getNextPage() {
		return nextPage;
	}
	public void setNextPage(URI nextPage) {
		this.nextPage = nextPage;
	}
	
	public TweetsPageResp(List<TweetResp> tweets, URI nextPage) {
		super();
		this.tweets = tweets;
		this.nextPage = nextPage;
	}
	
	public TweetsPageResp(List<TweetResp> tweets) {
		super();
		this.tweets = tweets;
		
	}
	
	public TweetsPageResp() {
	}
	
	
	

}

package com.twitter.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.twitter.models.PostTweetReq;
import com.twitter.models.TweetResp;
import com.twitter.models.TweetsPageResp;


public interface TweetService {
	
	public TweetResp createTweet(String xUsername, PostTweetReq postTweetReq);
	
    Page<TweetResp> findAll(Pageable pageable);
   
	TweetResp deleteTweet(String id, String xUsername);
	
	public TweetsPageResp getTweets(int offset, int limit, List<String> hashTag, List<String> username, HttpServletRequest request );

	/*
	Page<TweetResp> findAllByHashtagsIn(List<String> hashtags, Pageable pageable);
	
	Page<TweetResp> findAllByCreatedByIn(List<String> createdBy, Pageable pageable);
	
	Page<TweetResp> findByHashtagsInOrCreatedByIn(List<String> createdBy,List<String> hashtags, Pageable pageable);
	*/

}
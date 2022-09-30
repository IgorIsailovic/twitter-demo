package com.twitter.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.twitter.models.PostTweetReq;
import com.twitter.models.TweetResp;
import com.twitter.models.TweetsPageResp;


public interface TweetService {
	
	TweetResp createTweet(String xUsername, PostTweetReq postTweetReq);
	
    Page<TweetResp> findAll(Pageable pageable);
   
	TweetResp deleteTweet(String id, String xUsername);
	
	TweetsPageResp getTweets(int offset, int limit, List<String> hashtags, List<String> usernames, HttpServletRequest request );

}
package com.twitter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.twitter.models.TweetResp;


public interface TweetService {
	
	TweetResp createTweet(TweetResp tweet);
    
    Page<TweetResp> findAll(Pageable pageable);
   
	TweetResp deleteTweet(String id, String xUsername);

	Page<TweetResp> findAllByHashtagsIn(List<String> hashtags, Pageable pageable);
	
	Page<TweetResp> findAllByCreatedByIn(List<String> createdBy, Pageable pageable);
	
	Page<TweetResp> findByHashtagsInOrCreatedByIn(List<String> createdBy,List<String> hashtags, Pageable pageable);

}
package com.igor.igor.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.igor.igor.models.TweetResp;


public interface TweetRespService {


	List<TweetResp> findAll();
	
    void createTweet(TweetResp tweet);
    
    Page<TweetResp> findAll(Pageable pageable);
   

	String deleteTask(String id);

	TweetResp findById(String id);

	Page<TweetResp> findAllByHashtagsIn(List<String> hashtags, Pageable pageable);
	
	Page<TweetResp> findAllByCreatedByIn(List<String> createdBy, Pageable pageable);
	
	Page<TweetResp> findByHashtagsInOrCreatedByIn(List<String> createdBy,List<String> hashtags, Pageable pageable);


}
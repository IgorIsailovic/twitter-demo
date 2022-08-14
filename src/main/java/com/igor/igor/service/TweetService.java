package com.igor.igor.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.igor.igor.models.Tweet;


public interface TweetService {


	List<Tweet> findAll();
	
    void createTweet(Tweet tweet);
    
    Page<Tweet> findAll(Pageable pageable);
   

	String deleteTask(String id);

	Tweet findById(String id);

}
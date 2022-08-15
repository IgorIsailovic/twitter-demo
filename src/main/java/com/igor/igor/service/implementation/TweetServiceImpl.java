package com.igor.igor.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.igor.igor.models.Tweet;
import com.igor.igor.repository.TweetRepository;
import com.igor.igor.service.TweetService;

@Service
public class TweetServiceImpl implements TweetService {

	@Autowired
	TweetRepository tweetRepo;
	
	@Override
	public List<Tweet> findAll() {
		return tweetRepo.findAll();
	}

	@Override
	public void createTweet(Tweet tweet) {
	    	tweetRepo.save(tweet);
	
	}

	@Override
	public Page<Tweet> findAll(Pageable pageable) {
		return tweetRepo.findAll(pageable);
	}

	@Override
	public String deleteTask(String id) {
		tweetRepo.deleteById(id);
		return id;
	}

	@Override
	public Tweet findById(String id) {
		Tweet tweet = tweetRepo.findById(id).get();
		return tweet;
	}

	@Override
	public Page<Tweet> findByHashtags(String hashtags, Pageable pageable) {
		return tweetRepo.findByHashtags(hashtags, pageable);
	}

	@Override
	public Page<Tweet> findByCreatedBy(String createdBy, Pageable pageable) {
		return tweetRepo.findByCreatedBy(createdBy, pageable);
	}


}

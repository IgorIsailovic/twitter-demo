package com.igor.igor.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.igor.igor.models.TweetResp;
import com.igor.igor.repository.TweetRespRepository;
import com.igor.igor.service.TweetRespService;

@Service
public class TweetRespServiceImpl implements TweetRespService {

	@Autowired
	TweetRespRepository tweetRepo;
	
	@Override
	public List<TweetResp> findAll() {
		return tweetRepo.findAll();
	}

	@Override
	public void createTweet(TweetResp tweet) {
	    	tweetRepo.save(tweet);
	
	}

	@Override
	public Page<TweetResp> findAll(Pageable pageable) {
		return tweetRepo.findAll(pageable);
	}

	@Override
	public String deleteTask(String id) {
		tweetRepo.deleteById(id);
		return id;
	}

	@Override
	public TweetResp findById(String id) {
		TweetResp tweet = tweetRepo.findById(id).get();
		return tweet;
	}

	@Override
	public Page<TweetResp> findByHashtags(String hashtags, Pageable pageable) {
		return tweetRepo.findByHashtags(hashtags, pageable);
	}

	@Override
	public Page<TweetResp> findByCreatedBy(String createdBy, Pageable pageable) {
		return tweetRepo.findByCreatedBy(createdBy, pageable);
	}


}

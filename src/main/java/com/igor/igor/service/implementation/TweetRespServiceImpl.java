package com.igor.igor.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.igor.igor.error.TweetNotFoundException;
import com.igor.igor.models.TweetResp;
import com.igor.igor.repository.TweetRepository;
import com.igor.igor.service.TweetService;

@Service
public class TweetRespServiceImpl implements TweetService {

	@Autowired
	TweetRepository tweetRepo;
	
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
		TweetResp tweet = tweetRepo.findById(id).orElseThrow(() -> new TweetNotFoundException());
		return tweet;
	}

	@Override
	public Page<TweetResp> findAllByHashtagsIn(List<String> hashtags, Pageable pageable) {
		return tweetRepo.findAllByHashtagsIn(hashtags, pageable);
	}

	@Override
	public Page<TweetResp> findAllByCreatedByIn(List<String> createdBy, Pageable pageable) {
		return tweetRepo.findAllByCreatedByIn(createdBy, pageable);
	}

	@Override
	public Page<TweetResp> findByHashtagsInOrCreatedByIn(List<String> createdBy, List<String> hashtags,
			Pageable pageable) {
		return tweetRepo.findByHashtagsInOrCreatedByIn(createdBy, hashtags, pageable);
	}

	/*@Override
	public Page<TweetResp> findAllByCreatedByOrByHashtags(String createdBy,String hashtags, Pageable pageable){
		return tweetRepo.findAllByCreatedByOrByHashtags(createdBy, hashtags, pageable);
	}*/
	


}

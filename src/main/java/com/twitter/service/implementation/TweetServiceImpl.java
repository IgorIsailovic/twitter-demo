package com.twitter.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.exceptions.UnauthorizedTweetDeletionException;
import com.twitter.models.TweetResp;
import com.twitter.repository.TweetRepository;
import com.twitter.service.TweetService;

@Service
public class TweetServiceImpl implements TweetService {

	private TweetRepository tweetRepo;
	
	public TweetServiceImpl(TweetRepository tweetRepo) {
		this.tweetRepo = tweetRepo;
	}


	@Override
	public TweetResp createTweet(TweetResp tweet) {
	    	return tweetRepo.save(tweet);
	
	}

	@Override
	public Page<TweetResp> findAll(Pageable pageable) {
		return tweetRepo.findAll(pageable);
	}

	@Override
	public TweetResp deleteTweet(String id, String xUsername) {
		TweetResp tweet = tweetRepo.findById(id).orElseThrow(() -> new TweetNotFoundException("Specified tweet does not exist!"));// Throws TweetNotFoundException on service level
		if (tweet.getCreatedBy().equals(xUsername)) {
			tweetRepo.deleteById(id);
			return tweet;
		} else
			throw new UnauthorizedTweetDeletionException("Only allowed to delete own tweets!");
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

	
	


}

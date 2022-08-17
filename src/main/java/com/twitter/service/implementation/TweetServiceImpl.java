package com.twitter.service.implementation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.exceptions.UnauthorizedTweetDeletionException;
import com.twitter.models.PostTweetReq;
import com.twitter.models.TweetResp;
import com.twitter.models.TweetsPageResp;
import com.twitter.repository.TweetRepository;
import com.twitter.service.TweetService;
import com.twitter.util.Util;

@Service
public class TweetServiceImpl implements TweetService {

	private TweetRepository tweetRepo;
	
	public TweetServiceImpl(TweetRepository tweetRepo) {
		this.tweetRepo = tweetRepo;
	}


	@Override
	public TweetResp createTweet(String xUsername, PostTweetReq postTweetReq) {
		TweetResp tweet = new TweetResp();
		tweet.setCreatedBy(xUsername);
		tweet.setTweetBody(postTweetReq.getTweetBody());
		tweet.setHashtags(postTweetReq.getHashtags());
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
	public TweetsPageResp getTweets(int offset, int limit, List<String> hashTag, List<String> username, HttpServletRequest request  ) {
		TweetsPageResp response = new TweetsPageResp();
		Pageable paging = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
		Page<TweetResp> tweets;
		Util util = new Util();

		if (username != null && hashTag != null) {
			tweets = tweetRepo.findByHashtagsInOrCreatedByIn(hashTag, username, paging);
		} else if (hashTag != null) {
			tweets = tweetRepo.findAllByHashtagsIn(hashTag, paging);
		} else if (username != null) {
			tweets = tweetRepo.findAllByCreatedByIn(username, paging);
		} else
			tweets = tweetRepo.findAll(paging);

		if (tweets.isLast()) {
			response = new TweetsPageResp(tweets.getContent());
		} else {
			response = new TweetsPageResp(tweets.getContent(), util.uriFormater(request, paging, hashTag, username));
		}
		return response;	
	}
	
	/*
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
*/


}

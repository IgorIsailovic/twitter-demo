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
		TweetResp tweet = tweetRepo.findById(id)
				.orElseThrow(() -> new TweetNotFoundException("Specified tweet does not exist!"));

		if (tweet.getCreatedBy().equals(xUsername)) {
			tweetRepo.deleteById(id);
			return tweet;
		} else
			throw new UnauthorizedTweetDeletionException("Only allowed to delete own tweets!");
	}

	@Override
	public TweetsPageResp getTweets(int offset, int limit, List<String> hashtags, List<String> usernames,
			HttpServletRequest request) {
		TweetsPageResp response = new TweetsPageResp();
		Pageable paging = PageRequest.of(offset, limit, Sort.by("createdAt").descending());
		Page<TweetResp> tweets;
		Util util = new Util();

		if (usernames != null && hashtags != null) {
			tweets = tweetRepo.findByHashtagsInOrCreatedByIn(hashtags, usernames, paging);
		} else if (hashtags != null) {
			tweets = tweetRepo.findAllByHashtagsIn(hashtags, paging);
		} else if (usernames != null) {
			tweets = tweetRepo.findAllByCreatedByIn(usernames, paging);
		} else
			tweets = tweetRepo.findAll(paging);

		if (tweets.isLast()) {
			response = new TweetsPageResp(tweets.getContent());
		} else {
			response = new TweetsPageResp(tweets.getContent(), util.uriFormater(request, paging, hashtags, usernames));
		}
		return response;
	}

}

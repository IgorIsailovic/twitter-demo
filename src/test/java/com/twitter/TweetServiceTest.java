package com.twitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.exceptions.UnauthorizedTweetDeletionException;
import com.twitter.models.PostTweetReq;
import com.twitter.models.TweetResp;
import com.twitter.models.TweetsPageResp;
import com.twitter.repository.TweetRepository;
import com.twitter.service.TweetService;
import com.twitter.service.implementation.TweetServiceImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class TweetServiceTest {

	// @MockBean

	TweetRepository tweetRepository = mock(TweetRepository.class);

	TweetService tweetService = new TweetServiceImpl(tweetRepository);

	private String id = UUID.randomUUID().toString();
	private String xUsername = "X-username";
	private String body = "Body test";

	private List<String> hashtagsList = Arrays.asList("#testA", "#testB", "#testC");

	private List<String> usernames = Arrays.asList("xUsername", "xUsername1", "xUsername2");

	private int limit = 3;

	private Pageable paging = PageRequest.of(0, 3, Sort.by("createdAt").descending());

	private TweetResp tweet1 = new TweetResp(id, body, hashtagsList, xUsername, "Mon Aug 15 11:27:14 CEST 2022");
	private TweetResp tweet2 = new TweetResp(id, body, hashtagsList, xUsername, "Mon Aug 15 11:27:15 CEST 2022");
	private TweetResp tweet3 = new TweetResp(id, body, hashtagsList, xUsername, "Mon Aug 15 11:27:16 CEST 2022");

	private List<TweetResp> tweets = Arrays.asList(tweet1, tweet2, tweet3);

	@Test
	public void createTweetSuccess() {

		// given

		PostTweetReq postTweetReq = new PostTweetReq(body, hashtagsList);

		// when

		when(tweetRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

		TweetResp saved = tweetService.createTweet(xUsername, postTweetReq);

		// then

		assertEquals(saved.getTweetBody(), postTweetReq.getTweetBody());
		assertEquals(saved.getHashtags().size(), postTweetReq.getHashtags().size());

		verify(tweetRepository, times(1)).save(any());
	}

	@Test
	public void deleteTweetSuccess() {

		// given

		// when

		when(tweetRepository.findById(id)).thenReturn(Optional.of(tweet1));

		doNothing().when(tweetRepository).deleteById(id);

		TweetResp deleted = tweetService.deleteTweet(id, xUsername);

		// then

		assertEquals(deleted.getTweetId(), tweet1.getTweetId());

		assertEquals(deleted.getCreatedBy(), tweet1.getCreatedBy());

		verify(tweetRepository, times(1)).deleteById(any());

		verify(tweetRepository, times(1)).findById(any());

	}

	@Test
	public void deleteTweetThrowsUnauthorizedTweetDeletionException() {

		// given

		String exceptionMessage = "Users are only allowed to delete own tweets!";

		// when
		when(tweetRepository.findById(id)).thenReturn(Optional.of(tweet1));

		// then

		UnauthorizedTweetDeletionException thrown = assertThrows(UnauthorizedTweetDeletionException.class,
				() -> tweetService.deleteTweet(id, "x+Username"));

		assertTrue(thrown.getMessage().contains(exceptionMessage));

		verify(tweetRepository, never()).deleteById(any());
		verify(tweetRepository, times(1)).findById(any());
	}

	@Test
	public void deleteTweetThrowsTweetNotFoundException() {

		// given

		String exceptionMessage = "Specified tweet does not exist!";

		// when
		when(tweetRepository.findById(id)).thenReturn(Optional.empty());

		// then

		TweetNotFoundException thrown = assertThrows(TweetNotFoundException.class,
				() -> tweetService.deleteTweet(id, xUsername));

		assertTrue(thrown.getMessage().contains(exceptionMessage));

		verify(tweetRepository, never()).deleteById(any());
		verify(tweetRepository, times(1)).findById(any());

	}

	@Test
	public void getAllTweets() {

		// given

		Page<TweetResp> page = new PageImpl<>(tweets);

		HttpServletRequest request = mock(HttpServletRequest.class);

		// when

		when(tweetRepository.findAll(paging)).thenReturn(page);

		TweetsPageResp tweetPageResponse = tweetService.getTweets(0, limit, null, null, request);

		// then

		assertEquals(tweetPageResponse.getTweets().size(), tweets.size());
		assertTrue(tweetPageResponse.getTweets().size() <= limit);

		verify(tweetRepository, times(1)).findAll(paging);

	}

	@Test
	public void getTweetsWhenHashTagIsNotNull() {

		// given

		Page<TweetResp> page = new PageImpl<>(tweets);

		HttpServletRequest request = mock(HttpServletRequest.class);

		// when

		when(tweetRepository.findAllByHashtagsIn(hashtagsList, paging)).thenReturn(page);

		TweetsPageResp tweetPageResponse = tweetService.getTweets(0, limit, hashtagsList, null, request);

		// then

		assertEquals(tweetPageResponse.getTweets().size(), tweets.size());
		assertTrue(tweetPageResponse.getTweets().size() <= limit);

		verify(tweetRepository, times(1)).findAllByHashtagsIn(hashtagsList, paging);

	}

	@Test
	public void getTweetsWhenCreatedByIsNotNull() {

		// given

		Page<TweetResp> page = new PageImpl<>(tweets);

		HttpServletRequest request = mock(HttpServletRequest.class);

		// when

		when(tweetRepository.findAllByCreatedByIn(usernames, paging)).thenReturn(page);

		TweetsPageResp tweetPageResponse = tweetService.getTweets(0, limit, null, usernames, request);

		// then

		assertEquals(tweetPageResponse.getTweets().size(), tweets.size());
		assertTrue(tweetPageResponse.getTweets().size() <= limit);

		verify(tweetRepository, times(1)).findAllByCreatedByIn(usernames, paging);

	}

	@Test
	public void getTweetsWhenCreatedByAndHashtagAreNotNull() {

		// given

		Page<TweetResp> page = new PageImpl<>(tweets);

		HttpServletRequest request = mock(HttpServletRequest.class);

		// when

		when(tweetRepository.findByHashtagsInOrCreatedByIn(hashtagsList, usernames, paging)).thenReturn(page);

		TweetsPageResp tweetPageResponse = tweetService.getTweets(0, limit, hashtagsList, usernames, request);

		// then

		assertEquals(tweetPageResponse.getTweets().size(), tweets.size());
		assertTrue(tweetPageResponse.getTweets().size() <= limit);

		verify(tweetRepository, times(1)).findByHashtagsInOrCreatedByIn(hashtagsList, usernames, paging);

	}

}

package com.twitter;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.models.PostTweetReq;
import com.twitter.models.TweetResp;
import com.twitter.service.implementation.TweetServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TweetControllerTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;

	@MockBean
	TweetServiceImpl tweetService;

	private String id = UUID.randomUUID().toString();
	private String xUsername = "X-username";
	private String body = "Body test";

	private List<String> hashtagsList = Arrays.asList("#testA", "#testB", "#testC");

	private PostTweetReq postTweetReq = new PostTweetReq(body, hashtagsList);

	Pageable paging = PageRequest.of(0, 10, Sort.by("createdAt").descending());

	private TweetResp tweet1 = new TweetResp(id, body, hashtagsList, xUsername,
			"Mon Aug 15 11:27:14 CEST 2022");
	private TweetResp tweet2 = new TweetResp(id, body, hashtagsList, xUsername,
			"Mon Aug 15 11:27:15 CEST 2022");
	private TweetResp tweet3 = new TweetResp(id, body, hashtagsList, xUsername,
			"Mon Aug 15 11:27:16 CEST 2022");

	List<TweetResp> tweet = Arrays.asList(tweet1, tweet2, tweet3);

	@Test
	public void createTweet_success() throws Exception {

		Mockito.when(tweetService.createTweet(xUsername, postTweetReq)).thenReturn(tweet2);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/tweets")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-Username", "sbg_user1").content(this.mapper.writeValueAsString(tweet2)

				);

		mockMvc.perform(mockRequest).andExpect(status().isCreated());

	}

	@Test
	public void getAllRecords_success() throws Exception {

		Page<TweetResp> tweets = new PageImpl<>(tweet);

		Mockito.when(tweetService.findAll(paging)).thenReturn(tweets);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/tweets")
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-Username", "sbg_user1");

		mockMvc.perform(mockRequest).andExpect(status().isOk());

	}

	@Test
	public void deleteTweet_success() throws Exception {

		Mockito.when(tweetService.deleteTweet(id, xUsername)).thenReturn((tweet2));

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/v1/tweets/" + tweet2.getTweetId())
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)
				.header("X-Username", "sbg_user1").content(this.mapper.writeValueAsString(tweet1)

				);

		mockMvc.perform(mockRequest).andExpect(status().isOk());

	}
	
}

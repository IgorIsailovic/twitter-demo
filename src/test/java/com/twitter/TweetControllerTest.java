package com.twitter;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.controller.TweetController;
import com.twitter.models.TweetResp;
import com.twitter.repository.TweetRepository;
import com.twitter.service.implementation.TweetServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
public class TweetControllerTest {

	
	
	    @Autowired
	    MockMvc mockMvc;
	    @Autowired
	    ObjectMapper mapper;
	    
	
	    
	    @MockBean
	    TweetRepository tweetResponseRepository;
	    
	    @MockBean
	    TweetServiceImpl tweetService;
	    
	    
	   
    	
	    List<String> hashtagsList = Arrays.asList("#testA","#testB","#testC"); 
	    Pageable paging = PageRequest.of(0, 10, Sort.by("createdAt").descending());
	    
	    TweetResp TWEET_1 = new TweetResp ("sdsdsadsaas","Test Tweet 1", hashtagsList, "sbg_user1", "Mon Aug 15 11:27:14 CEST 2022");
	    TweetResp TWEET_2 = new TweetResp ("ssads321dsa","Test Tweet 2", hashtagsList, "sbg_user3", "Mon Aug 15 11:27:15 CEST 2022");
	    TweetResp TWEET_3 = new TweetResp ("sd1123wwwww","Test Tweet 3", hashtagsList, "sbg_user2", "Mon Aug 15 11:27:16 CEST 2022");
	    

	    List<TweetResp> tweet = Arrays.asList(TWEET_1, TWEET_2, TWEET_3);
	    
	    //Page<TweetResp> tweets = new PageImpl<>(tweet);

	  

	    @Test
	    public void createTweet_success() throws Exception {
	    

	    	
	    Mockito.when(tweetResponseRepository.save(TWEET_2)).thenReturn(TWEET_2);
	    
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/v1/tweets")
	    		.contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .header("X-Username", "sbg_user1")
	            .content(this.mapper.writeValueAsString(TWEET_2)       		

	            		);
	    
	    mockMvc.perform(mockRequest)
	    		.andExpect(status().isCreated())
	    		.andExpect(jsonPath("$",notNullValue()))
	    		.andExpect(jsonPath("$.createdBy", is("sbg_user1")));
	    
	    
	    }
	    
	    @Test
	    public void getAllRecords_success() throws Exception {
	    
		    Page<TweetResp> tweets = new PageImpl<>(tweet);

		    
	        Mockito.when(tweetResponseRepository.findAll(paging)).thenReturn(tweets);
	        
	        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/v1/tweets")
		    		.contentType(MediaType.APPLICATION_JSON)
		            .accept(MediaType.APPLICATION_JSON)
		            .header("X-Username", "sbg_user1");

		/*   mockMvc.perform(mockRequest)
			.andExpect(status().isOk());
	        */
	    	
	    	
	    	
	    }
	    
	    @Test
	    public void deleteTweet_success() throws Exception {
	    
		  

	    	
	    Mockito.when(tweetResponseRepository.findById(TWEET_2.getTweetId())).thenReturn(Optional.of(TWEET_2));
	   
	    MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/v1/tweets/" + TWEET_2.getTweetId())
	    		.contentType(MediaType.APPLICATION_JSON)
	            .accept(MediaType.APPLICATION_JSON)
	            .header("X-Username", "sbg_user1")
	            .content(this.mapper.writeValueAsString(TWEET_2)       		

	            		);
	    
	    mockMvc.perform(mockRequest)
		.andExpect(status().isOk())
		.andExpect(jsonPath("$",notNullValue()))
		.andExpect(jsonPath("$.createdBy", is("sbg_user1")));
	    
	  
	    }
}

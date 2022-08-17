package com.twitter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import com.twitter.exceptions.TweetNotFoundException;
import com.twitter.exceptions.UnauthorizedTweetDeletionException;
import com.twitter.models.TweetResp;
import com.twitter.repository.TweetRepository;
import com.twitter.service.TweetService;
import com.twitter.service.implementation.TweetServiceImpl;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;




@SpringBootTest
public class TweetServiceTest {
	
   // @MockBean
	
    TweetRepository tweetResponseRepository = mock(TweetRepository.class);
    
   
    TweetService tweetService = new TweetServiceImpl(tweetResponseRepository);
    

    @Test
    public void createTweetSuccess() {

    	//given
    	
	    List<String> hashtagsList = Arrays.asList("#testA","#testB","#testC"); 

    	TweetResp tweetResp = new TweetResp("sdsdsadsaas","Test Tweet 1", hashtagsList, "sbg_user1", "Mon Aug 15 11:27:14 CEST 2022");
    	
    	//when
    	
    	when(tweetResponseRepository.save(tweetResp)).thenReturn(tweetResp);
    
    	TweetResp saved = tweetService.createTweet(tweetResp);
    	
    	//then
    	
    	assertEquals(saved.getTweetId(), tweetResp.getTweetId());
    	assertEquals(saved.getHashtags().size(), tweetResp.getHashtags().size());

    
    }
    

    @Test
    public void deleteTweetSuccess() {

    	//given
    	
	    List<String> hashtagsList = Arrays.asList("#testA","#testB","#testC"); 

    	
    	String id = UUID.randomUUID().toString();
    	
    	String xUsername = "X-username";
    	
    	TweetResp tweetResp = new TweetResp(id,"Test Tweet 1", hashtagsList, xUsername, "Mon Aug 15 11:27:14 CEST 2022");
    	
    	//when
    	
    	when(tweetResponseRepository.findById(id)).thenReturn(Optional.of(tweetResp));
    	
    	doNothing().when(tweetResponseRepository).deleteById(id);
    	
    	TweetResp deleted = tweetService.deleteTweet(id, xUsername);
    	
    	//then
    	
    	assertEquals(deleted.getTweetId(), tweetResp.getTweetId());

    	assertEquals(deleted.getCreatedBy(), tweetResp.getCreatedBy());

    
    }
   
    @Test
    public void deleteTweetThrowsUnauthorizedTweetDeletionException() {

    	//given
	    List<String> hashtagsList = Arrays.asList("#testA","#testB","#testC"); 
    	
    	String id = UUID.randomUUID().toString();
    	String xUsername = "X-username";
    	TweetResp tweetResp = new TweetResp(id,"Test Tweet 1", hashtagsList, "xUsername", "Mon Aug 15 11:27:14 CEST 2022");
    	
    	String exceptionMessage = "Only allowed to delete own tweets!";
    	
    	//when
    	when(tweetResponseRepository.findById(id)).thenReturn(Optional.of(tweetResp));
    	    	
    	
    	//then
    	
    	    UnauthorizedTweetDeletionException thrown = assertThrows(
    	    		UnauthorizedTweetDeletionException.class,
    	           () ->  tweetService.deleteTweet(id, xUsername)
    	    );

    	    assertTrue(thrown.getMessage().contains(exceptionMessage));
    	}

    @Test
    public void deleteTweetThrowsTweetNotFoundException() {

    	//given
    	
    	String id = UUID.randomUUID().toString();
    	String xUsername = "X-username";    	
    	String exceptionMessage = "Specified tweet does not exist!";
    	
    	//when
    	when(tweetResponseRepository.findById(id)).thenReturn(Optional.empty());
    	    	
    	
    	//then
    	
    	TweetNotFoundException thrown = assertThrows(
    			TweetNotFoundException.class,
    	           () ->  tweetService.deleteTweet(id, xUsername)
    	    );

    	    assertTrue(thrown.getMessage().contains(exceptionMessage));
    	}


    
    }


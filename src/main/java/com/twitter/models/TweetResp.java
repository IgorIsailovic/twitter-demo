package com.twitter.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Index;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;




@Entity
@Table(name = "tweets", indexes = {
		  @Index(name = "tweetIdIndex", columnList = "tweet_id"),
		  @Index(name = "createdByIndex", columnList = "created_by"),
		  
		})
public class TweetResp {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "tweet_id",columnDefinition = "CHAR(32)")
    private String tweetId;

    //@NotNull(message="Tweet body must be specified!")
    @Column(name = "tweet_body", columnDefinition = "text", nullable=false)
    //@Size(max=320, message = "Tweet body must not be greater than 320 characters!")
    private String tweetBody;
    
    
    @ElementCollection(fetch = FetchType.EAGER) 
    @CollectionTable(name = "hash_tags", joinColumns = @JoinColumn(name = "tweet_id"), indexes = {@Index(name = "hashTagIndex", columnList = "hash_tags"),
})
    @Column(name="hash_tags")
   // @Size(max=5, message = "Hashtag list must not be greater than 5!")
    private List</*@Pattern(regexp = "^#[a-zA-Z]{2,16}$", message="Each Hash Tag must follow the following pattern ^#[a-zA-Z]{2,16}$ ")*/ String> hashtags;
 
   
   // @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$")
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_at")
    private String createdAt = new Date().toString();
    

	public String getTweetId() {
		return tweetId;
	}


	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}


	public String getTweetBody() {
		return tweetBody;
	}


	public void setTweetBody(String tweetBody) {
		this.tweetBody = tweetBody;
	}


	public List<String> getHashtags() {
		return hashtags;
	}


	public void setHashtags(List<String> hashtags) {
		this.hashtags = hashtags;
	}

	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}


	public TweetResp(String tweetId,  String tweetBody, List<String> hashtags, String createdBy, String createdAt) {
		super();
		this.tweetId = tweetId;
		this.tweetBody = tweetBody;
		this.hashtags = hashtags;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		
	}

	
	public TweetResp() {
	
	}
	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", tweetBody=" + tweetBody + ", hashtags=" + hashtags + ", createdBy="
				+ createdBy + ", createdAt=" + createdAt + "]";
	}



	

	

    
    
	
}

package com.igor.igor.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Required;




@Entity
@Table(name = "tweet")

public class Tweet {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "tweet_id",columnDefinition = "CHAR(32)")
    private String tweetId;

    @NotNull(message="Tweet body must be specified!")
    @Column(name = "tweet_body", columnDefinition = "text", nullable=false)
    @Size(max=320, message = "Tweet body must not be greater than 320 characters!")
    private String tweetBody;
    
    
    @ElementCollection
    @CollectionTable(name = "hash_tags", joinColumns = @JoinColumn(name = "tweet_id"))
    @Column(name="hash_tags")
    @Size(max=5, message = "Hashtag list must not be greater than 5!")
    private List<@Pattern(regexp = "^#[a-zA-Z]{2,16}$", message="Each Hash Tag must follow the following pattern ^#[a-zA-Z]{2,16}$ ") String> hashtags;
    
   
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,32}$")
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


	public Tweet(String tweetId,  String tweetBody, List<String> hashtags, String createdBy, String createdAt) {
		super();
		this.tweetId = tweetId;
		this.tweetBody = tweetBody;
		this.hashtags = hashtags;
		this.createdBy = createdBy;
		this.createdAt = createdAt;
		
	}

	
	public Tweet() {
	
	}
	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", tweetBody=" + tweetBody + ", hashtags=" + hashtags + ", createdBy="
				+ createdBy + ", createdAt=" + createdAt + "]";
	}



	

	

    
    
	
}

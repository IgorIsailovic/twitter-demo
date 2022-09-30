package com.twitter.models;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class PostTweetReq {

	@NotNull(message = "Tweet body must be specified!")
	@Size(max = 320, message = "Tweet body must not contain more than 320 characters!")
	private String tweetBody;

	@Size(max = 5, message = "No more than 5 hashtags can be specified!")
	private List<@Pattern(regexp = "^#[a-zA-Z]{2,16}$", message = "Each hashtag must follow the following pattern ^#[a-zA-Z]{2,16}$") String> hashtags;

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

	public PostTweetReq() {

	}

	public PostTweetReq(String tweetBody, List<String> hashtags) {
		super();
		this.tweetBody = tweetBody;
		this.hashtags = hashtags;
	}

}

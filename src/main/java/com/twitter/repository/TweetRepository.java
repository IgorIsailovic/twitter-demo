package com.twitter.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.twitter.models.TweetResp;


public interface TweetRepository extends JpaRepository<TweetResp, String> {
	
	Page<TweetResp> findAll(Pageable pageable);
	Page<TweetResp> findAllByHashtagsIn(List<String> hashtags, Pageable pageable);
	Page<TweetResp> findAllByCreatedByIn(List<String> createdBy, Pageable pageable);
	Page<TweetResp> findByHashtagsInOrCreatedByIn( List<String> hashtags, List<String> createdBy, Pageable pageable);

}

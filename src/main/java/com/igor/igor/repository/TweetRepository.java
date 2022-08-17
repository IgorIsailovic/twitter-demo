package com.igor.igor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.igor.igor.models.TweetResp;

public interface TweetRepository extends JpaRepository<TweetResp, String> {
	Page<TweetResp> findAll(Pageable pageable);
	Page<TweetResp> findAllByHashtagsIn(List<String> hashtags, Pageable pageable);
	Page<TweetResp> findAllByCreatedByIn(List<String> hashtag, Pageable pageable);
	Page<TweetResp> findByHashtagsInOrCreatedByIn( List<String> createdBy,List<String> hashtags, Pageable pageable);

	

	
	

}

package com.igor.igor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.igor.igor.models.TweetResp;

public interface TweetRespRepository extends JpaRepository<TweetResp, String> {
	Page<TweetResp> findAll(Pageable pageable);
	Page<TweetResp> findByHashtags(String hashtags, Pageable pageable);
	Page<TweetResp> findByCreatedBy(String createdBy, Pageable pageable);
	
	

}

package com.igor.igor.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.igor.igor.models.Tweet;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
	Page<Tweet> findAll(Pageable pageable);

}

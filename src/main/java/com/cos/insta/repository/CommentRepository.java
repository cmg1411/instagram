package com.cos.insta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.insta.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer>{
	
}

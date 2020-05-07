package com.cos.insta.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.insta.model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Integer>{
	
	//unFollow
	@Transactional
	int deleteByFromUserIdAndToUserId(int fromUserId, int toUserId);
	
	//팔로우 유무
	int countByFromUserIdAndToUserId(int fromUserId, int toUserId);
	
	//팔로우리스트(하얀버튼)
	List<Follow> findByFromUserId(int fromUserId);
	
	//팔로잉리스트(맞팔 체크 후 버튼 색 결정)
	List<Follow> findByToUserId(int toUserId);
		
}

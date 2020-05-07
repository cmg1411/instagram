package com.cos.insta.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.insta.model.Follow;
import com.cos.insta.model.User;
import com.cos.insta.repository.FollowRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.service.MyUserDetails;

@Controller
public class FollowController {

	@Autowired
	private UserRepository mUserRepository;
	@Autowired
	private FollowRepository mFollowRepository;
	
	
	@PostMapping("/follow/{id}")
	public @ResponseBody String follow(@AuthenticationPrincipal MyUserDetails userDetail, 
									   @PathVariable int id) {
		User fromUser = userDetail.getUser();
		
		Optional<User> oToUser = mUserRepository.findById(id);
		User toUser = oToUser.get();
		
		Follow follow = new Follow();
		follow.setFromUser(fromUser);
		follow.setToUser(toUser);
		
		mFollowRepository.save(follow);
		
		return "ok";
	}
	
	@DeleteMapping("/follow/{id}")
	public @ResponseBody String unFollow(@AuthenticationPrincipal MyUserDetails userDetail, 
									   @PathVariable int id) {
		User fromUser = userDetail.getUser();
		
		Optional<User> oToUser = mUserRepository.findById(id);
		User toUser = oToUser.get();
		
		mFollowRepository.deleteByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
		
		List<Follow> follows = mFollowRepository.findAll();
		
		return "ok"; //ResponseEntity로 수정
	}
	
	@GetMapping("follow/follower/{id}")
	public String followFollower(@PathVariable int id,
								 @AuthenticationPrincipal MyUserDetails userDetail,
								 Model model) {
		
		//팔로워리스트
		List<Follow> followers = mFollowRepository.findByToUserId(id);
		
		List<Follow> principalFollowers = mFollowRepository.findByFromUserId(userDetail.getUser().getId());
		
		for(Follow f1:followers) {
			for(Follow f2:principalFollowers) {
				if(f1.getFromUser().getId()==f2.getToUser().getId()) {
					f1.setFollowState(true);
				}
			}
		}
		
		model.addAttribute("followers",followers);
		return "follow/follower";
	}
	
	@GetMapping("follow/follow/{id}")
	public String followFollow(@PathVariable int id, 
							   @AuthenticationPrincipal MyUserDetails userDetail,
								Model model) {
		
		//팔로우리스트
		List<Follow> follows = mFollowRepository.findByFromUserId(id);
		
		List<Follow> principalFollows = mFollowRepository.findByFromUserId(userDetail.getUser().getId());
		
		for(Follow f1:follows) {
			for(Follow f2:principalFollows) {
				if(f1.getToUser().getId()==f2.getToUser().getId()) {
					f1.setFollowState(true);
				}
			}
		}
		
		model.addAttribute("follows",follows);
		return "follow/follow";
	}
	
}

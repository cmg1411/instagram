package com.cos.insta.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cos.insta.model.User;
import com.cos.insta.repository.FollowRepository;
import com.cos.insta.repository.UserRepository;
import com.cos.insta.service.MyUserDetails;

@Controller
public class UserController {
	
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired // 자동으로 알맞은 Bean을 주입해준다.
	private BCryptPasswordEncoder encoder;

	@Autowired
	private UserRepository mUserRepository;
	
	@Autowired
	private FollowRepository mFollowRepository;
	
	@GetMapping("/auth/login")
	public String authLogin() {
		return "auth/login";
	}

	@GetMapping("/auth/join") //get방식 mapping
	public String authJoin() {
		return "auth/join";
	}

	@PostMapping("/auth/joinProc") //post방식 mapping
	public String authJoinProc(User user) {
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		log.info("rawPassword : " + rawPassword);
		log.info("encPassword : " + encPassword);

		mUserRepository.save(user);

		return "redirect:/auth/login";
	}
	
	@GetMapping("/user/{id}")
	public String profile(@PathVariable int id,
						  @AuthenticationPrincipal MyUserDetails userDetail,
						  Model model) {
		
		//id를 통해 해당 유저를 검색(이미지 + 유저정보)
		/*
		 * 1.imageCount 
		 * 2.followerCount 
		 * 3.followingCount 
		 * 4.User 오브젝트(Image(likeCount)컬렉션)
		 * 5. followCheck 팔로우 유무(1이면 팔로우 1아니면 언팔)
		 */
		
		//4. 수정필요
		Optional<User> oToUser = mUserRepository.findById(id);
		User user = oToUser.get();
		model.addAttribute("user", user);
		
		//5.
		User principal = userDetail.getUser();
		
		int followCheck = mFollowRepository.countByFromUserIdAndToUserId(principal.getId(), id);
		log.info("followCheck : " + followCheck);
		model.addAttribute("followCheck", followCheck);
		
		return "user/profile";
	}
	
	@GetMapping("/user/edit/{id}")
	public String userEdit(@PathVariable int id) {
		//해당 ID로 Select해서 수정
		//findByUserInfo()사용 (만들어야함.)
		
		return "user/profile_edit";
	}
}
